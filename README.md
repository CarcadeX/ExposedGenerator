# ExposedGenerator

Table generator for Exposed framework

<h2>Basic Usage</h2>
Instruction for kotlin v. 1.8.21
<h3>Step 1  - configure build.gradle</h3>
<p>Add <b>Kotlin 1.8.21</b> and <b>KSP</b> plugins:</p>

```kotlin
kotlin("jvm") version "1.8.21"
id("com.google.devtools.ksp") version "1.8.21-1.0.11"
```

<p>Add <b>Exposed</b> and <b>ExposedGenerator</b> to project:</p>

```kotlin
dependencies {
    implementation("org.jetbrains.exposed:exposed-core:0.40.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.40.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")

    compileOnly("io.github.iredtea:exposedgenerator:[LATEST_VERSION]")
    ksp("io.github.iredtea:exposedgenerator:[LATEST_VERSION]")
}
```

<h3>Step 2 - create data class</h3>
<p>Create data class that you want to save to db. Use types 
that supported by ExposedGenerator!</p>

```kotlin
data class SomeData(
    val id: Int,
    val name: String,
    val count: Double
)

```

<h3>Step 3 - annotate</h3>
Annotate it as ExposedTable. <br>
Set @ID annotation at key field of your class. <br>
Set @Length annotation to fields that needed it. <br>
Set other required annotations.


```kotlin
@ExposedTable
data class SomeData(
    @ID(true)
    val id: Int,
    @Length(10)
    val name: String,
    val count: Double
)

```

<h3>Step 4 - run generator</h3>
Run <b>kspKotlin</b> gradle task.
It will generate method toDAO()
for your class instance, that returns
DAO of your instance.
Also you can use toData() method
in DAO for get instance of data class
from DAO.

<h3>Step 5 - configure exposed Database</h3>
Official documentation: <a href="https://github.com/JetBrains/Exposed/wiki/Getting-Started">Exposed Documentation</a>

<h3>Step 6 - create table</h3>
Create table using SchemaUtils.create([TABLE NAME]) in
exposed transaction. <br>
You can set name of table in argument of ExposedTable
annotation. <br>
By default, name is name of your data class + 's' or 'es'.<br>

For example:

```kotlin
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
```

<h2>Supported types</h2>

| Type        | Exposed Type | Requires annotations |
|-------------|--------------|----------------------|
| Byte        | byte         | No                   |
| UByte       | ubyte        | No                   |
| Short       | short        | No                   |
| UShort      | ushort       | No                   |
| Int         | integer      | No                   |
| UInt        | uinteger     | No                   |
| Long        | long         | No                   |
| ULong       | ulong        | No                   |
| Float       | float        | No                   |
| Double      | double       | No                   |
| BigDecimal  | decimal      | No                   |
| Boolean     | bool         | No                   |
| Char        | char         | No                   |
| ByteArray   | binary       | No                   |
| ExposedBlob | blob         | No                   |
| UUID        | uuid         | No                   |
| String      | varchar      | Length               |
| Collection  | reference    | CollectionRef        |
| Other table | reference    | RefOnDAO             |