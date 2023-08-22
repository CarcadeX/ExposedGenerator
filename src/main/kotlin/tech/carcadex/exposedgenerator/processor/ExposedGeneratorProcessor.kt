package tech.carcadex.exposedgenerator.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.validate
import tech.carcadex.exposedgenerator.annotations.ExposedTable


class ExposedGeneratorProcessor(private val codeGenerator: CodeGenerator,
                                private val logger: KSPLogger) : SymbolProcessor {
    private var invoked = false
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) {
            return emptyList()
        }

        val classes = resolver.getSymbolsWithAnnotation(tech.carcadex.exposedgenerator.annotations.ExposedTable::class.qualifiedName!!)

        classes.let { classes -> classes.forEach {
                it.accept(tech.carcadex.exposedgenerator.processor.TableVisitor(codeGenerator, logger), Unit)
            }
        }

        invoked = true
        return classes.filter { it.validate() }.toList()
    }


}