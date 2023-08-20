package me.redtea.exposedgenerator.processor.mapper.impl

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import me.redtea.exposedgenerator.annotations.Length
import me.redtea.exposedgenerator.processor.mapper.AnnotationMapper
import me.redtea.exposedgenerator.processor.model.Field
import kotlin.reflect.KClass

object LengthMapper : AnnotationMapper<Length>() {
    override val annotation: KClass<Length> = Length::class

    override fun map(field: Field, property: KSPropertyDeclaration, annotation: Length): Field {
        val len = "${annotation.length}"
        val args = field.args.ifEmpty { mutableListOf("") }
        args[0] = len
        field.args = args
        return field
    }

}