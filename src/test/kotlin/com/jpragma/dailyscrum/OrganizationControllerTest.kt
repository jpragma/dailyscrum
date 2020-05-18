package com.jpragma.dailyscrum

import com.jpragma.dailyscrum.dao.OrganizationRepository
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.retrieveList
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

@MicronautTest
internal class OrganizationControllerTest(private val embeddedServer: EmbeddedServer) {
    private val client: RxHttpClient by lazy {
        val client: RxHttpClient = embeddedServer.applicationContext.createBean(RxHttpClient::class.java, embeddedServer.url)
        client
    }

    @Test
    fun getAllTeamsTest() {
        val request = HttpRequest.GET<Any>("/organization/team")
        val teams = client.toBlocking().retrieveList<Team>(request)
        expectThat(teams).hasSize(1)
        teams[0].run {
            expectThat(name) isEqualTo "Ogres"
        }
    }

    @MockBean(OrganizationRepository::class)
    fun mockOrgRepo(): OrganizationRepository {
        val isaac = Member("Isaac", "Dev")
        val alex = Member("Alex", "Dev")
        val teams = listOf(Team("Ogres", OrganizationUnit("CORE-DEV"), listOf(isaac, alex)))

        val mock = mockk<OrganizationRepository>()
        every {
            mock.getAllTeams()
        } returns teams

        return mock
    }
}
