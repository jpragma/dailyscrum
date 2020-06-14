package com.jpragma.dailyscrum.dao

import org.jetbrains.exposed.sql.transactions.transaction

fun withRollback(testBlock: () -> Unit) {
    transaction {
        testBlock()
        rollback()
    }
}