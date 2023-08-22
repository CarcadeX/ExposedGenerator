package tech.carcadex.exposedgenerator.processor.mapper.impl

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import tech.carcadex.exposedgenerator.annotations.ExposedTable
import tech.carcadex.exposedgenerator.annotations.ID
import tech.carcadex.exposedgenerator.annotations.RefOnDAO
import tech.carcadex.exposedgenerator.processor.mapper.AnnotationMapper
import tech.carcadex.exposedgenerator.processor.model.Field
import tech.carcadex.exposedgenerator.processor.name
import java.io.File
import kotlin.reflect.KClass

object RefMapper : AnnotationMapper<tech.carcadex.exposedgenerator.annotations.RefOnDAO>() {
    override val annotation: KClass<tech.carcadex.exposedgenerator.annotations.RefOnDAO> = tech.carcadex.exposedgenerator.annotations.RefOnDAO::class

    override fun map(field: Field, property: KSPropertyDeclaration, annotation: tech.carcadex.exposedgenerator.annotations.RefOnDAO): Field {
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
