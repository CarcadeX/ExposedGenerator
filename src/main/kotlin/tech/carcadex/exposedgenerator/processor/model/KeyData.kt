package tech.carcadex.exposedgenerator.processor.model

data class KeyData(val field: Field, val autoInc: Boolean) {
    fun generateString(): String {
        return """
        override val id: Column<EntityID<${field.typeName()}>> = ${field.sqlType}("${field.name}"${if(field.args.isNotEmpty()) ", " + field.args.joinToString(", ") else ""})${if(autoInc)".autoIncrement()" else ""}.entityId()
            override val primaryKey: PrimaryKey = PrimaryKey(id)
        """.trimIndent()
    }
}