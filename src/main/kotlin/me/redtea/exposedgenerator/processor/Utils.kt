package me.redtea.exposedgenerator.processor

import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName

fun replaceKotlinDefType(s: String): String = if(s.startsWith("kotlin.")) s.replace("kotlin.", "") else s
@OptIn(KotlinPoetKspPreview::class)
fun className(type: KSType): String = replaceKotlinDefType(type.toClassName().canonicalName)
fun KSType.name() = className(this)
@OptIn(KotlinPoetKspPreview::class)
fun KSType.packageName(): String = this.toClassName().packageName

val exposedTypes = mapOf(
    "Byte" to "byte",
    "UByte" to "ubyte",
    "Short" to "short",
    "UShort" to "ushort",
    "Int" to "integer",
    "UInt" to "uinteger",
    "Long" to "long",
    "ULong" to "ulong",
    "Float" to "float",
    "Double" to "double",
    "java.math.BigDecimal" to "decimal",
    "Boolean" to "bool",
    "Char" to "char",
    "ByteArray" to "binary",
    "org.jetbrains.exposed.sql.statements.api.ExposedBlob" to "blob",
    "java.util.UUID" to "uuid",
    "String" to "varchar",
)

fun sqlType(type: KSTypeReference): String = sqlType(type.resolve())
fun sqlType(type: KSType): String = exposedTypes[type.name()] ?: ""