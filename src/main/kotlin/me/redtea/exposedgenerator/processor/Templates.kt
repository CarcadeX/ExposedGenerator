package me.redtea.exposedgenerator.processor

fun collectionTable(tableName: String, packageName: String, type: String, sqlType: String, typeArgs: List<String>, ownerTable: String, ownerDao: String): String = """
package $packageName  
                            
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@me.redtea.exposedgenerator.annotations.GeneratedTable
object $tableName : IntIdTable() {
    val value = $sqlType("value"${if(typeArgs.isNotEmpty()) ", " + typeArgs.joinToString("\n") else ""})
    val owner = reference("owner", ${ownerTable})
}

class ${tableName}DAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<${tableName}DAO>(${tableName}) {
        fun collection(owner: ${ownerDao}): Collection<${type}> = ${tableName}DAO.all().filter { it.owner == owner }.map { it.value }
        fun collection(col: Collection<${type}>, owner: ${ownerDao}) {
            ${tableName}DAO.all().filter { it.owner == owner }.forEach { it.delete() }
            col.forEach {
                ${tableName}DAO.new {
                    value = it
                    this.owner = owner
                }
            }
        }
    }
    
    var value by $tableName.value
    var owner by ${ownerDao} referencedOn ${tableName}.owner
    

}
"""