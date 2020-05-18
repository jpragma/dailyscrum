package com.jpragma.dailyscrum

import com.jpragma.dailyscrum.dao.OrgUnitEntity
import com.jpragma.dailyscrum.dao.TeamEntity
import com.jpragma.dailyscrum.dao.TeamTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Singleton
import javax.sql.DataSource

interface OrganizationService {
    fun getAllTeams(): List<Team>
    fun getOrgUnits(): List<OrganizationUnit>
}

@Singleton
internal class OrganizationServiceImpl(private val dataSource: DataSource) : OrganizationService {
    override fun getOrgUnits(): List<OrganizationUnit> {
        Database.connect(dataSource)
        val all = transaction {
            OrgUnitEntity.all().toList()
        }
        return all.map { it.toOrgUnit() }
    }

    override fun getAllTeams(): List<Team> {
        Database.connect(dataSource)
        val all = transaction {
            TeamEntity.all().toList().map { it.toTeam() }
        }
        return all
    }
}


