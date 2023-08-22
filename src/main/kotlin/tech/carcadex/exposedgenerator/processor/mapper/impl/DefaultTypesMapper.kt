package tech.carcadex.exposedgenerator.processor.mapper.impl

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import tech.carcadex.exposedgenerator.processor.mapper.FieldMapper
import tech.carcadex.exposedgenerator.processor.model.Field
import tech.carcadex.exposedgenerator.processor.sqlType

object DefaultTypesMapper : FieldMapper {

    override fun map(field: Field, property: KSPropertyDeclaration): Field {
        if(field.sqlType == "") {
            field.sqlType = sqlType(property.type)
        }
        return field
    }
}