package com.jpragma.dailyscrum

data class OrganizationUnit(var id: Int = -1, val name: String, val teams: List<Team> = emptyList())
data class Team(var id:Int = -1, val name: String)
data class Member(var id:Int = -1, val teamId: Int, val name: String, var title: String, var avatar: String? = null)
