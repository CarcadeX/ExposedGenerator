package tech.carcadex.exposedgenerator.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ExposedTable(val name: String = "")