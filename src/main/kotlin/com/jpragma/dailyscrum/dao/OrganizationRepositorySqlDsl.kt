package com.jpragma.dailyscrum.dao

import com.jpragma.dailyscrum.OrganizationUnit
import com.jpragma.dailyscrum.Team
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
internal class OrganizationRepositorySqlDsl(dataSource: DataSource) : OrganizationRepository {
    init {
        Database.connect(dataSource)
    }

    override fun getAllOrgUnits(): List<OrganizationUnit> {
        return transaction {
            OrgUnitTable.selectAll().map {
                val orgUnitId = it[OrgUnitTable.id].value
                OrganizationUnit(orgUnitId, it[OrgUnitTable.name], getTeams(orgUnitId))
            }.toList()
        }
    }

    private fun getTeams(orgUnitId: Int): List<Team> {
        val query = TeamTable.select { TeamTable.orgUnit eq orgUnitId }
        return query.map {
            Team(it[TeamTable.id].value, it[TeamTable.name])
        }.toList()
    }

    override fun createOrgUnit(orgUnit: OrganizationUnit): OrganizationUnit {
        return transaction {
            val generatedId = OrgUnitTable.insertAndGetId {
                it[name] = orgUnit.name
            }
            orgUnit.id = generatedId.value

            orgUnit.teams.forEach {
                createTeamRecord(generatedId, it)
            }
            orgUnit
        }
    }

    private fun createTeamRecord(orgUnitId: EntityID<Int>, team: Team) {
        val teamId = TeamTable.insertAndGetId {
            it[orgUnit] = orgUnitId
            it[name] = team.name
        }
        team.id = teamId.value
    }

    override fun updateOrgUnit(orgUnit: OrganizationUnit): OrganizationUnit {
        return transaction {
            OrgUnitTable.update({ OrgUnitTable.id eq orgUnit.id }) {
                it[name] = orgUnit.name
            }
            TeamTable.deleteWhere {
                (TeamTable.orgUnit eq orgUnit.id) and (TeamTable.id notInList orgUnit.teams.map { it.id })

            }
            orgUnit.teams.forEach {team ->
                if (team.id < 0) {
                    createTeamRecord(EntityID(orgUnit.id, OrgUnitTable), team)
                } else {
                    TeamTable.update({ TeamTable.id eq team.id }) {
                        it[name] = team.name
                    }
                }
            }
            orgUnit
        }
    }

    override fun deleteOrgUnit(id: Int): OrganizationUnit {
        return transaction {
            val hasNoTeams = TeamTable.select { TeamTable.orgUnit eq id }.empty()
            require(hasNoTeams) {"Can not delete OrganizationUnit that has teams"}
            val orgUnit = OrgUnitTable.select { OrgUnitTable.id eq id }.map {
                OrganizationUnit(it[OrgUnitTable.id].value, it[OrgUnitTable.name])
            }.first()
            OrgUnitTable.deleteWhere { OrgUnitTable.id eq id }
            orgUnit
        }
    }
}