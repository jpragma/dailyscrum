package com.jpragma.dailyscrum

import com.jpragma.dailyscrum.dao.OrganizationRepository
import io.micronaut.http.annotation.*
import org.slf4j.LoggerFactory

@Controller("/organization")
class OrganizationController(private val organizationRepository: OrganizationRepository) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Get("/")
    fun getAllOrgUnits(): List<OrganizationUnit> {
        return organizationRepository.getAllOrgUnits()
    }

    @Post("/")
    fun createOrgUnit(orgUnit: OrganizationUnit): OrganizationUnit {
        return organizationRepository.createOrgUnit(orgUnit.copy(id = -1))
    }

    @Put("/")
    fun updateOrgUnit(orgUnit: OrganizationUnit) {
        organizationRepository.updateOrgUnit(orgUnit)
    }

    @Delete("/{id}")
    fun deleteOrgUnit(@PathVariable("id") id: Int): OrganizationUnit {
        return organizationRepository.deleteOrgUnit(id)
    }

}