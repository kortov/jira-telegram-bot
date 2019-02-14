package com.github.mikesafonov.jira.telegram.service

import com.github.mikesafonov.jira.telegram.dto.Event
import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.File
import java.io.StringReader
import java.io.StringWriter
import javax.annotation.PostConstruct

private val logger = KotlinLogging.logger {}

@Service
class MustacheProcessorService {

    @Value("\${jira.event.template.path}")
    var templatePath: String? = null
    lateinit var mustache: Mustache

    @PostConstruct
    fun postConstruct() {
        mustache = if (templatePath.isNullOrBlank()) {
            logger.info { "Using default template" }
            readDefaultTemplate()
        } else {
            try {
                logger.info { "Reading template $templatePath" }
                readTemplate()
            } catch (e: Exception) {
                logger.error(e.message, e)
                logger.info { "Using default template" }
                readDefaultTemplate()
            }
        }
    }

    private fun readTemplate(): Mustache {
        val templateText = File(templatePath).readText()
        return readTemplateFromString(templateText)
    }

    private fun readDefaultTemplate(): Mustache {
        val defaultTemplate = ClassPathResource("default.mustache").file.readText()
        return readTemplateFromString(defaultTemplate)
    }

    private fun readTemplateFromString(template: String): Mustache {
        val mf = DefaultMustacheFactory()
        return mf.compile(StringReader(template), "template")
    }

    fun processEvent(event: Event, template: String): String {
        val sw = StringWriter()
        mustache.execute(sw, event).flush()
        return sw.toString()
    }
}