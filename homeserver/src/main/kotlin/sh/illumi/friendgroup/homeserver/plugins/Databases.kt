package sh.illumi.friendgroup.homeserver.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.ktorm.database.Database
import org.ktorm.entity.filter
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.ktorm.schema.BaseTable
import org.ktorm.schema.ColumnDeclaring
import sh.illumi.friendgroup.homeserver.HomeserverConfig
import kotlin.coroutines.coroutineContext

object DB {
    private val connectionMutex = Mutex()
    private lateinit var _db: Database

    suspend fun connect() {
        connectionMutex.withLock {
            Class.forName("org.postgresql.Driver")
            val dbConfig = HomeserverConfig.INSTANCE.db
            _db = Database.connect(
                url = dbConfig.url,
                user = dbConfig.user,
                password = dbConfig.password
            )
        }
    }

    suspend fun getConnection() = connectionMutex.withLock {
        _db
    }
}

fun Application.configureDatabases() {
    launch {
        DB.connect()
    }
}

suspend inline fun <E : Any, T : BaseTable<E>> T.getList(predicate: (T) -> ColumnDeclaring<Boolean>): List<E> {
    return DB.getConnection().sequenceOf(this).filter(predicate).toList()
}

