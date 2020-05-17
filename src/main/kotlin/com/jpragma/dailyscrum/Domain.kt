package com.jpragma.dailyscrum

data class OrganizationUnit(val name: String)
data class Member(val name: String, var title: String, var avatar: String? = null)
data class Team(val name: String, val unit: OrganizationUnit, val members: List<Member>, val lead: Member)