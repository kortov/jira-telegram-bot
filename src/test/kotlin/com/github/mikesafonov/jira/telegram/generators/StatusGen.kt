package com.github.mikesafonov.jira.telegram.generators

import com.github.mikesafonov.jira.telegram.dto.Status
import io.kotlintest.properties.Gen

/**
 * @author Mike Safonov
 */
class StatusGen : Gen<Status> {
    companion object {
        fun generateDefault(): Status {
            return StatusGen().generateOne()
        }

        fun empty(): Status? {
            return null
        }
    }

    override fun constants(): Iterable<Status> {
        return emptyList()
    }

    override fun random(): Sequence<Status> {
        return generateSequence {
            generateOne()
        }
    }

    fun generateOne(
        id: String = Gen.string().random().first(),
        description: String = Gen.string().random().first(),
        name: String = Gen.string().random().first()
    ): Status {
        return Status(id, description, name)
    }

}