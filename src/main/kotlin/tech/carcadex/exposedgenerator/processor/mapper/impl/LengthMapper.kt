package tech.carcadex.exposedgenerator.processor.mapper.impl

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import tech.carcadex.exposedgenerator.annotations.Length
import tech.carcadex.exposedgenerator.processor.mapper.AnnotationMapper
import tech.carcadex.exposedgenerator.processor.model.Field
import kotlin.reflect.KClass

object LengthMapper : AnnotationMapper<tech.carcadex.exposedgenerator.annotations.Length>() {
    override val annotation: KClass<tech.carcadex.exposedgenerator.annotations.Length> = tech.carcadex.exposedgenerator.annotations.Length::class

    override fun map(field: Field, property: KSPropertyDeclaration, annotation: tech.carcadex.exposedgenerator.annotations.Length): Field {
        val len = "${annotation.length}"
        val args = field.args.ifEmpty { mutableListOf("") }
        args[0] = len
        field.args = args
        return field
    }

}