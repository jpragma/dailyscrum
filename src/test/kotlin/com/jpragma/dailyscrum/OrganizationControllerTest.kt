package com.jpragma.dailyscrum

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.retrieveList
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

@MicronautTest
internal class OrganizationControllerTest(private val embeddedServer: EmbeddedServer) {
    private val client: RxHttpClient by lazy {
        val client: RxHttpClient = embeddedServer.applicationContext.createBean(RxHttpClient::class.java, embeddedServer.url)
        client
    }

    @Test
    fun getAllPodsTest() {
        val request = HttpRequest.GET<Any>("/organization/pod")
        val pods = client.toBlocking().retrieveList<Pod>(request)
        expectThat(pods).hasSize(1)
        pods[0].run {
            expectThat(name) isEqualTo "Ogres"
        }
    }
}
