package com.jpragma.dailyscrum.dao

import com.jpragma.dailyscrum.Member
import com.jpragma.dailyscrum.OrganizationUnit
import com.jpragma.dailyscrum.Team
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrgUnitEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<OrgUnitEntity>(OrgUnitTable)

    var name by OrgUnitTable.name
    val teams by TeamEntity referrersOn TeamTable.orgUnit

    fun toOrgUnit() = OrganizationUnit(id.value, name, teams.map { it.toTeam() })
}

class TeamEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TeamEntity>(TeamTable)

    var orgUnit by OrgUnitEntity referencedOn TeamTable.orgUnit
    var name by TeamTable.name

    fun toTeam() = Team(id.value, name)
}

class MemberEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<MemberEntity>(MemberTable)

    var teamId by MemberTable.team
    var name by MemberTable.name
    var title by MemberTable.title
    var avatar by MemberTable.avatar

    fun toMember() = Member(id.value, teamId.value, name, title, avatar)
}