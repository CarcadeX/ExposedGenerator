package me.redtea.exposedgenerator.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import me.redtea.exposedgenerator.annotations.ExposedTable
import me.redtea.exposedgenerator.annotations.ID
import me.redtea.exposedgenerator.processor.mapper.FieldMapService
import me.redtea.exposedgenerator.processor.model.Field
import me.redtea.exposedgenerator.processor.model.KeyData
import me.redtea.exposedgenerator.processor.model.TableContext
import java.io.OutputStreamWriter



class TableVisitor(private val codeGenerator: CodeGenerator,
                    private val logger: KSPLogger
) : KSVisitorVoid() {
    private var keyDataNullable: KeyData? = null
    private var fields = mutableListOf<Field>()
    private lateinit var tableName: String
    private lateinit var className: String
    private lateinit var packageName: String

    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        packageName = classDeclaration.packageName.asString()
        className = classDeclaration.simpleName.asString()
        val fromAnnotation = classDeclaration.getAnnotationsByType(ExposedTable::class).first().name
        tableName = if(fromAnnotation != "") fromAnnotation else (if(className.endsWith("s")) className + "es" else className + "s")

        classDeclaration.getDeclaredProperties().forEach { it.accept(this, Unit) }
        if(keyDataNullable == null) throw IdNotPresentException()
        val keyData = keyDataNullable!!



        try {
            codeGenerator.createNewFile(Dependencies.ALL_FILES, packageName, className + "Exposed", "kt").use { output ->
                OutputStreamWriter(output).use { writer ->
                    writer.write(
                        """
package ${packageName}  
                            
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

//Table for ${tableName}
@me.redtea.exposedgenerator.annotations.GeneratedTable
object $tableName : IdTable<${keyData.field.typeName()}>() {
    ${fields.joinToString("\n    ") { it.generateString() }}
    ${keyData.generateString()}
}

//DAO ${className}
class ${className}DAO(${keyData.field.name}: EntityID<${keyData.field.typeName()}>) : Entity<${keyData.field.typeName()}>(${keyData.field.name}) {
    companion object : EntityClass<${keyData.field.typeName()}, ${className}DAO>($tableName)
    ${fields.joinToString("\n    ") { it.generateDaoString(tableName) }}
}

//Converter ${className}
object ${className}Converter {
    fun ${className}.toDAO(): ${className}DAO {
        val dao = ${className}DAO.findById(this@toDAO.${keyData.field.name})
        return if(dao == null) ${className}DAO.new(this@toDAO.${keyData.field.name}) {
            ${fields.joinToString("\n            ") { it.generateToDaoValue() }}
        } else ${className}DAO[this@toDAO.${keyData.field.name}].let {
            with(it) {
                ${fields.joinToString("\n                ") { it.generateToDaoValue() }}
            }
            it
        }
    }
    fun ${className}DAO.toData(): $className =
        ${className}DAO[this@toData.id].let {
            ${className}(it.id.value, ${fields.joinToString(", ") { "${it.name} = ${it.generateToDataValue()}" }})
        }
    
    fun ${className}.updateDAO() {
        toDAO()
    }
}
""".trimIndent())
                }
            }
        } catch(e: FileAlreadyExistsException) {}

    }

    @OptIn(KspExperimental::class)
    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
        val field = FieldMapService.map(
            Field(property.simpleName.getShortName(), property.type.resolve(), "", context = TableContext(
            tableName,
            className + "DAO",
            className,
            packageName,
            codeGenerator
        )
            ), property)
        if(field.sqlType == "") throw UnsupportedTypeException(property.type.resolve())
        if(field.sqlType == "#ignored") field.sqlType = ""
        if(property.isAnnotationPresent(ID::class)) {
            keyDataNullable = KeyData(field, property.getAnnotationsByType(ID::class).first().autoIncrement)
        } else fields.add(field)
    }
}