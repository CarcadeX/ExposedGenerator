package tech.carcadex.exposedgenerator.processor.model

import com.google.devtools.ksp.processing.CodeGenerator

data class TableContext(val tableName: String, val daoName: String, val className: String, val packageName: String, val codeGenerator: CodeGenerator)
