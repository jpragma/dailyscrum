package com.jpragma.dailyscrum.dao

import org.jetbrains.exposed.dao.id.IntIdTable

object OrgUnitTable : IntIdTable("ORG_UNIT") {
    val name = varchar("name", 255)
}

object TeamTable : IntIdTable("TEAM") {
    val orgUnit = reference("org_unit_id", OrgUnitTable)
    val name = varchar("name", 255)
}

object MemberTable : IntIdTable("MEMBER") {
    val team = reference("team_id", TeamTable)
    val name = varchar("name", 255)
    val title = varchar("title", 255)
    val avatar = varchar("avatar", 1024).nullable()
}
