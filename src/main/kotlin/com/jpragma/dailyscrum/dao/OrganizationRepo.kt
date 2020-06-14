package com.jpragma.dailyscrum.dao

import com.jpragma.dailyscrum.OrganizationUnit
import com.jpragma.dailyscrum.Team

interface OrganizationRepository {
    fun getAllOrgUnits(): List<OrganizationUnit>
    fun createOrgUnit(orgUnit: OrganizationUnit): OrganizationUnit
    fun updateOrgUnit(orgUnit: OrganizationUnit): OrganizationUnit
    fun deleteOrgUnit(id: Int): OrganizationUnit

}

