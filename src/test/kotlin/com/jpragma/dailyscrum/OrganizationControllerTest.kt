package com.jpragma.dailyscrum

import com.jpragma.dailyscrum.dao.OrganizationRepository
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.retrieveList
import io.micronaut.http.retrieveObject
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.get
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.propertiesAreEqualTo
import javax.inject.Inject

@MicronautTest
@Property(name = "datasources.default.driverClassName", value = "") // disables creation of DataSource bean
internal class OrganizationControllerTest(private val embeddedServer: EmbeddedServer) {
    private val testOrgUnit = OrganizationUnit(5, "CORE-DEV", listOf(Team(101, "DEV1")))

    private val client: RxHttpClient by lazy {
        val client: RxHttpClient = embeddedServer.applicationContext.createBean(RxHttpClient::class.java, embeddedServer.url)
        client
    }

    @Inject
    private lateinit var repo: OrganizationRepository

    @Test
    fun getAllOrgUnitsTest() {
        every { repo.getAllOrgUnits() } returns listOf(testOrgUnit)
        val request = HttpRequest.GET<Any>("/organization")
        val actualOrgUnits = client.toBlocking().retrieveList<OrganizationUnit>(request)
        expectThat(actualOrgUnits).hasSize(1)[0].propertiesAreEqualTo(testOrgUnit)
    }

    @Test
    fun createOrgUnitTest() {
        val request = HttpRequest.POST<Any>("/organization", testOrgUnit)
        expectThat(client.toBlocking().retrieveObject<OrganizationUnit>(request)) isEqualTo testOrgUnit.copy(id = 99)
        verify {
            repo.createOrgUnit(match {
                it.id == -1 && it.name == testOrgUnit.name
            })
        }
    }

    @Test
    fun updateOrgUnitTest() {
        val request = HttpRequest.PUT("/organization", testOrgUnit)
        client.toBlocking().exchange(request, Unit::class.java)
        verify { repo.updateOrgUnit(testOrgUnit) }
    }

    @Test
    fun deleteOrgUnitTest() {
        every { repo.deleteOrgUnit(any()) } returns testOrgUnit
        val request = HttpRequest.DELETE<Any>("/organization/111")
        expectThat(client.toBlocking().retrieveObject<OrganizationUnit>(request)) isEqualTo testOrgUnit
        verify { repo.deleteOrgUnit(111) }
    }

    @MockBean(OrganizationRepository::class)
    fun mockOrgRepo(): OrganizationRepository {
        val mock = mockk<OrganizationRepository>()
        every { mock.updateOrgUnit(any()) } answers { arg(0)}
        every { mock.createOrgUnit(any()) } answers {
            arg<OrganizationUnit>(0).copy(id = 99)
        }
        return mock
    }
}
