package tech.carcadex.exposedgenerator.processor.mapper

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import tech.carcadex.exposedgenerator.processor.model.Field

interface FieldMapper {
    fun map(field: Field, property: KSPropertyDeclaration): Field
}