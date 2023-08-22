package tech.carcadex.exposedgenerator.processor.model

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName
import tech.carcadex.exposedgenerator.processor.replaceKotlinDefType

open class Field(val name: String, val type: KSType, var sqlType: String, var args: MutableList<String> = mutableListOf(), val context: TableContext) {
    open fun generateString(): String {
        return "val $name = ${sqlType}(\"$name\"${if(args.isNotEmpty()) ", " + args.joinToString(", ") else ""  })"
    }
    open fun generateDaoString(tableName: String): String {
        return "var $name by $tableName.$name"
    }
    open fun generateToDaoValue(): String {
        return "this.$name = this@toDAO.$name"
    }
    open fun generateToDataValue(): String {
        return "it.$name"
    }

    @OptIn(KotlinPoetKspPreview::class)
    open fun typeName(): String {
        val res = type.toClassName().canonicalName
        return replaceKotlinDefType(res)
    }
}
