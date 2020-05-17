package com.jpragma.dailyscrum

import javax.inject.Singleton
import javax.sql.DataSource

interface OrganizationService {
    fun getAllTeams(): List<Team>
}

@Singleton
internal class OrganizationServiceImpl(private val dataSource: DataSource) : OrganizationService {
    override fun getAllTeams(): List<Team> {
        TODO("Not yet implemented")
    }
}


