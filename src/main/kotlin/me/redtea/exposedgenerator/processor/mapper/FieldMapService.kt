package me.redtea.exposedgenerator.processor.mapper

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import me.redtea.exposedgenerator.annotations.CollectionRef
import me.redtea.exposedgenerator.annotations.Length
import me.redtea.exposedgenerator.annotations.RefOnDAO
import me.redtea.exposedgenerator.processor.mapper.impl.*
import me.redtea.exposedgenerator.processor.model.Field
import me.redtea.exposedgenerator.processor.mapper.impl.*
import java.io.File

object FieldMapService {
    private val mappers: MutableCollection<FieldMapper> = mutableSetOf(DefaultTypesMapper, LengthMapper)
    private val verifyMappers = mutableSetOf(StringVerifyMapper)
    private val annotationConflictMappers = mutableMapOf(
        RefOnDAO::class.java to RefMapper,
        CollectionRef::class.java to CollectionMapper
    )
    @OptIn(KspExperimental::class)
    fun map(field: Field, property: KSPropertyDeclaration): Field {
        var res = field
        mappers.forEach {
            res = it.map(field, property)
        }
        annotationConflictMappers.forEach {
            if(property.isAnnotationPresent(it.key.kotlin)) {
                res = annotationConflictMappers[it.key]!!.map(field, property)
                return@forEach
            }
        }
        verifyMappers.forEach {
            it.map(field, property)
        }
        return res
    }
    fun register(fieldMapper: FieldMapper) {
        mappers.add(fieldMapper)
    }
}
