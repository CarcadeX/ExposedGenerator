package me.redtea.exposedgenerator.processor

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName

@OptIn(KotlinPoetKspPreview::class)
class UnsupportedTypeException(type: KSType) : Exception("Type ${type.toClassName().canonicalName} not supported")
class IdNotPresentException : Exception("You must set id of table by @ID annotation")
