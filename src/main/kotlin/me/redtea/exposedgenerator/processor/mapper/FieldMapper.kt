package me.redtea.exposedgenerator.processor.mapper

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import me.redtea.exposedgenerator.processor.model.Field

interface FieldMapper {
    fun map(field: Field, property: KSPropertyDeclaration): Field
}