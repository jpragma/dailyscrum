package com.jpragma.dailyscrum

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory

@Controller("/organization")
class OrganizationController {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Get("/pod")
    fun getAllPods(): List<Pod> {
        val isaac = Member("Isaac", "Dev")
        val alex = Member("Alex", "Dev")
        return listOf(Pod("Ogres", OrganizationUnit("CORE-DEV"), listOf(isaac, alex), isaac))
    }
}