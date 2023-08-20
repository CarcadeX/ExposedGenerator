package me.redtea.example

import me.redtea.exposedgenerator.annotations.ExposedTable
import me.redtea.exposedgenerator.annotations.ID
import me.redtea.exposedgenerator.annotations.Length
import me.redtea.example.SomeDataConverter.toDAO
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


@ExposedTable
data class SomeData(
    @ID(true)
    val id: Int,
    @Length(10)
    val name: String,
    val count: Double
)


fun main() {
    val data = SomeData(1, "ABC", 1.0)
    val db = Database.connect("jdbc:sqlite:sample.db")
    transaction(db) {
        SchemaUtils.create(SomeDatas)
        data.toDAO().name = "New Name"
        println(SomeDataDAO[1].name)
        //OUTPUT: New Name
    }

}