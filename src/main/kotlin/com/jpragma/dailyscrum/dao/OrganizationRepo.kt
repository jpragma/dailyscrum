package com.jpragma.dailyscrum.dao

import com.jpragma.dailyscrum.Member
import com.jpragma.dailyscrum.OrganizationUnit
import com.jpragma.dailyscrum.Team
import io.micronaut.context.annotation.Requires
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Singleton
import javax.sql.DataSource

interface OrganizationRepository {
    fun getAllTeams(): List<Team>
    fun getOrgUnits(): List<OrganizationUnit>
}

@Singleton
@Requires(property = "kotlin.exposed", value = "dao", defaultValue = "dao")
internal class OrganizationRepositoryDao(dataSource: DataSource) : OrganizationRepository {
    init {
        Database.connect(dataSource)
    }

    override fun getOrgUnits(): List<OrganizationUnit> {
        val all = transaction {
            OrgUnitEntity.all().toList()
        }
        return all.map { it.toOrgUnit() }
    }

    override fun getAllTeams(): List<Team> {
        return transaction {
            TeamEntity.all().toList().map { it.toTeam() }
        }
    }
}

@Singleton
@Requires(property = "kotlin.exposed", value = "dsl")
internal class OrganizationRepositorySqlDsl(dataSource: DataSource) : OrganizationRepository {
    init {
        Database.connect(dataSource)
    }

    override fun getOrgUnits(): List<OrganizationUnit> {
        return transaction {
            OrgUnitTable.selectAll().map {
                OrganizationUnit(it[OrgUnitTable.name])
            }.toList()
        }
    }

    override fun getAllTeams(): List<Team> {
        return transaction {
            (OrgUnitTable innerJoin TeamTable).selectAll().map {
                val members = MemberTable
                        .select {MemberTable.team eq it[TeamTable.id]}
                        .map { memberRow -> Member(memberRow[MemberTable.name], memberRow[MemberTable.title], memberRow[MemberTable.avatar]) }
                        .toList()
                Team(it[TeamTable.name], OrganizationUnit(it[OrgUnitTable.name]), members)
            }.toList()
        }
    }

}

