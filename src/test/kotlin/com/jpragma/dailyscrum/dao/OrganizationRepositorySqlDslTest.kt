package com.jpragma.dailyscrum.dao

import com.jpragma.dailyscrum.OrganizationUnit
import com.jpragma.dailyscrum.Team
import io.micronaut.test.annotation.MicronautTest
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.*
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject

@MicronautTest
internal class OrganizationRepositorySqlDslTest {
    private val randomString
        get() = UUID.randomUUID().toString()

    @Inject
    private lateinit var repo: OrganizationRepository

    @Test
    fun orgUnitCreateAndDelete() {
        withRollback {
            val orgUnit = OrganizationUnit(
                    id = -1,
                    name = randomString,
                    teams = listOf(Team(id=-1, name = randomString))
            )
            val created = repo.createOrgUnit(orgUnit)
            expectThat(created.id) isGreaterThan 0
            val persisted = getOrgUnitById(created.id)

            expectThat(persisted).propertiesAreEqualTo(orgUnit)
        }
    }

    @Test
    fun orgUnitUpdate() {
        withRollback {
            val orgUnit = OrganizationUnit(
                    id = -1,
                    name = randomString,
                    teams = listOf(Team(id=-1, name = "foo001"), Team(id=-1, name = "foo002"))
            )
            val created = repo.createOrgUnit(orgUnit)

            val toUpdate = created.copy(
                    name = "<new org unit name>",
                    teams = listOf(created.teams[0].copy(name = "foo000"), Team(-1, name = "foo003"))
            )
            val updated = repo.updateOrgUnit(toUpdate)
            updated.run {
                expectThat(name).isEqualTo(toUpdate.name)
                expectThat(teams.all { it.id > 0 }).isTrue()
                expectThat(teams.map { it.name }).containsExactly("foo000", "foo003")
            }
            expectThat(updated).propertiesAreEqualTo(getOrgUnitById(created.id))
        }
    }

    private fun getOrgUnitById(id: Int): OrganizationUnit {
        return repo.getAllOrgUnits().find { it.id == id } ?: throw IllegalStateException("OrganizationUnit with id: $id is not found")
    }


}