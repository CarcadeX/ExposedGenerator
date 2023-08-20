package me.redtea.exposedgenerator.processor.mapper.impl

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import me.redtea.exposedgenerator.processor.mapper.FieldMapper
import me.redtea.exposedgenerator.processor.model.Field
import me.redtea.exposedgenerator.processor.sqlType

object DefaultTypesMapper : FieldMapper {

    override fun map(field: Field, property: KSPropertyDeclaration): Field {
        if(field.sqlType == "") {
            field.sqlType = sqlType(property.type)
        }
        return field
    }
}