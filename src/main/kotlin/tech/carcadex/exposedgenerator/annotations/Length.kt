package tech.carcadex.exposedgenerator.annotations

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class Length(val length: Int)
