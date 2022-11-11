package data.Tables

import org.jetbrains.exposed.sql.Table

object UserTable:Table() {

    val email = varchar("email",50)
    var name = varchar("name",50)
    val hpassword = varchar("hpassword",50)

    override val primaryKey: PrimaryKey = PrimaryKey(email)

}