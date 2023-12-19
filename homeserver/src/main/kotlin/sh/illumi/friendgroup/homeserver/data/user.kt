package sh.illumi.friendgroup.homeserver.data

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.resources.*
import io.ktor.util.*
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.or
import org.ktorm.entity.Entity
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import sh.illumi.friendgroup.homeserver.plugins.getList
import java.sql.Connection
import kotlin.text.toCharArray

object UserFriends : Table<UserFriend>("r_user_friends") {
    val idUser1 = int("idUser1").primaryKey().references(Users) { it.user1 }
    val idUser2 = int("idUser2").primaryKey().references(Users) { it.user2 }
}

interface UserFriend : Entity<UserFriend> {
    companion object : Entity.Factory<UserFriend>()

    val user1: User
    val user2: User
}

object Users : Table<User>("t_users") {
    val id = int("id").primaryKey().bindTo { it.id }
    val displayName = varchar("name").bindTo { it.displayName }
    private val passwordHash = varchar("passwordHash").bindTo { it.passwordHash }
}

interface User : Entity<User> {
    companion object : Entity.Factory<User>() {
        fun hashPassword(password: String): String =
            BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    val id: Int
    var displayName: String
    var passwordHash: String

    suspend fun getFriends(predicate: (UserFriends) -> ColumnDeclaring<Boolean>): List<User> = UserFriends
        .getList { (it.idUser1 eq Users.id).or(it.idUser2 eq Users.id).and(predicate(it)) }
        .fold(mutableListOf()) { friends, userFriend ->
            if (userFriend.user1.id == id) friends += userFriend.user2
            else if (userFriend.user2.id == id) friends += userFriend.user1
            friends
        }

    fun checkPassword(attemptedPass: String): Boolean {
        val result = BCrypt.verifyer().verify(attemptedPass.toCharArray(), passwordHash.toCharArray())
        return result.verified
    }
}