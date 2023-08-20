package me.redtea.exposedgenerator.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.validate
import me.redtea.exposedgenerator.annotations.ExposedTable


class ExposedGeneratorProcessor(private val codeGenerator: CodeGenerator,
                                private val logger: KSPLogger) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val classes = resolver.getSymbolsWithAnnotation(ExposedTable::class.qualifiedName!!)

        classes.let { classes -> classes.forEach {
                it.accept(TableVisitor(codeGenerator, logger), Unit)
            }
        }
        return classes.filter { it.validate() }.toList()
    }


}