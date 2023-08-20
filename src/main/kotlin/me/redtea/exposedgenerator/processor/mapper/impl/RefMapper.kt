package me.redtea.exposedgenerator.processor.mapper.impl

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import me.redtea.exposedgenerator.annotations.ExposedTable
import me.redtea.exposedgenerator.annotations.ID
import me.redtea.exposedgenerator.annotations.RefOnDAO
import me.redtea.exposedgenerator.processor.mapper.AnnotationMapper
import me.redtea.exposedgenerator.processor.model.Field
import me.redtea.exposedgenerator.processor.name
import java.io.File
import kotlin.reflect.KClass

object RefMapper : AnnotationMapper<RefOnDAO>() {
    override val annotation: KClass<RefOnDAO> = RefOnDAO::class

    override fun map(field: Field, property: KSPropertyDeclaration, annotation: RefOnDAO): Field {
        field.sqlType = "reference"
        field.args = mutableListOf(annotation.tableName)
        return object : Field(field.name, field.type, field.sqlType, field.args, field.context) {
            override fun generateDaoString(tableName: String): String = "var $name by ${annotation.dao} referencedOn ${field.context.tableName}.${field.name}"
            override fun generateToDaoValue(): String {
                return "this.$name = ${annotation.dao}.findById(this@toDAO.$name)!!"
            }
            override fun generateToDataValue(): String {
                return "${field.name}.id.value"
            }
        }
    }
}
