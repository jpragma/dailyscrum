package com.jpragma.dailyscrum

import io.micronaut.http.HttpStatus
import io.micronaut.runtime.mapError
import io.micronaut.runtime.startApplication
import java.lang.RuntimeException

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        startApplication<Application>(*args) {
            packages("com.jpragma.dailyscrum")
            mapError<RuntimeException> { HttpStatus.INTERNAL_SERVER_ERROR.code }
        }
    }
}
