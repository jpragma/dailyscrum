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

    fun toOrgUnit() = OrganizationUnit(name = name)
}

class TeamEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TeamEntity>(TeamTable)

    var orgUnit by OrgUnitEntity referencedOn TeamTable.orgUnit
    var name by TeamTable.name
    val members by MemberEntity referrersOn MemberTable.team

    fun toTeam() = Team(name, orgUnit.toOrgUnit(), members.map { it.toMember() })
}

class MemberEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<MemberEntity>(MemberTable)

    var name by MemberTable.name
    var title by MemberTable.title
    var avatar by MemberTable.avatar

    fun toMember() = Member(name = name, title = title, avatar = avatar)
}