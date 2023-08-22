package tech.carcadex.exposedgenerator.processor.mapper.impl

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import tech.carcadex.exposedgenerator.processor.mapper.FieldMapper
import tech.carcadex.exposedgenerator.processor.model.Field
import tech.carcadex.exposedgenerator.processor.name

object StringVerifyMapper : FieldMapper {
    override fun map(field: Field, property: KSPropertyDeclaration): Field {
        if(field.type.name() == "String" && field.args.isEmpty()) {
            throw IllegalArgumentException("String requires to set len")
        }
        return field
    }
}