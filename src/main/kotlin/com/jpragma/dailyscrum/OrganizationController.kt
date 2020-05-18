package com.jpragma.dailyscrum

import com.jpragma.dailyscrum.dao.OrganizationRepository
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory

@Controller("/organization")
class OrganizationController(private val organizationRepository: OrganizationRepository) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Get("/")
    fun getAllOrgUnits(): List<OrganizationUnit> {
        return organizationRepository.getOrgUnits()
    }

    @Get("/team")
    fun getAllTeams(): List<Team> {
        log.info("Retrieving all teams")
        return organizationRepository.getAllTeams()
    }


}