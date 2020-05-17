package com.jpragma.dailyscrum

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory

@Controller("/organization")
class OrganizationController(private val organizationService: OrganizationService) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Get("/team")
    fun getAllTeams(): List<Team> {
        log.info("Retrieving all teams")
        return organizationService.getAllTeams()
    }
}