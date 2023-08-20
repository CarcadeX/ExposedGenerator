package me.redtea.exposedgenerator.processor.mapper.impl

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import me.redtea.exposedgenerator.annotations.CollectionRef
import me.redtea.exposedgenerator.processor.className
import me.redtea.exposedgenerator.processor.collectionTable
import me.redtea.exposedgenerator.processor.mapper.AnnotationMapper
import me.redtea.exposedgenerator.processor.model.Field
import me.redtea.exposedgenerator.processor.sqlType
import java.io.File
import java.io.OutputStreamWriter
import kotlin.reflect.KClass

object CollectionMapper : AnnotationMapper<CollectionRef>() {
    override val annotation: KClass<CollectionRef> = CollectionRef::class

    override fun map(field: Field, property: KSPropertyDeclaration, annotation: CollectionRef): Field {
        val tableCol = field.context.className + "" + if(annotation.collectionName == "") field.name.capitalize() else annotation.collectionName;
        val typeCol = className(field.type.arguments[0].type!!.resolve());
        try {
            field.context.codeGenerator.createNewFile(Dependencies.ALL_FILES, field.context.packageName, field.context.className + "ExposedCollections",
                "kt").use { output ->
                OutputStreamWriter(output).use { writer ->
                    writer.write(
                        collectionTable(tableCol,
                        field.context.packageName,
                        typeCol,
                        sqlType(field.type.arguments[0].type!!.resolve()),
                        field.args,
                        field.context.tableName,
                        field.context.daoName
                    )
                    )
                }
            }
        } catch (ignored: FileAlreadyExistsException) {}

        field.sqlType = "#ignored"
        return object : Field(field.name, field.type, field.sqlType, field.args, field.context) {
            override fun generateString(): String = ""
            override fun generateDaoString(tableName: String): String = "fun ${field.name}(): Collection<${typeCol}> = ${tableCol}DAO.collection(this)\n" +
                    "   fun ${field.name}(col: Collection<${typeCol}>) { ${tableCol}DAO.collection(col, this) }"
            override fun generateToDaoValue(): String {
                return "${tableCol}DAO.collection(this@toDAO.${field.name}, this)"
            }
            override fun generateToDataValue(): String {
                return "${tableCol}DAO.collection(it)"
            }
        }
    }
}