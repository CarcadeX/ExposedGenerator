package tech.carcadex.exposedgenerator.annotations

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class RefOnDAO(val tableName: String, val dao: String)
