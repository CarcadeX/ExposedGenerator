package me.redtea.exposedgenerator.processor.mapper

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import me.redtea.exposedgenerator.processor.model.Field
import java.io.File
import kotlin.reflect.KClass

abstract class AnnotationMapper<T : Annotation> : FieldMapper {
    abstract val annotation: KClass<T>

    abstract fun map(field: Field, property: KSPropertyDeclaration, annotation: T): Field

    @OptIn(KspExperimental::class)
    override fun map(field: Field, property: KSPropertyDeclaration): Field {
        if(property.isAnnotationPresent(annotation)) {
            File("C:\\Users\\redtea\\Desktop\\Java Projects\\ExposedGenerator\\log").writeText(
                File("C:\\Users\\redtea\\Desktop\\Java Projects\\ExposedGenerator\\log").readText() + "\n" +
                "${annotation} field ${field.name}")
            return map(field, property, property.getAnnotationsByType(annotation).first())
        }
        return field
    }
}