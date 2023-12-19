package sh.illumi.friendgroup.homeserver

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class DBConfig(
    val url: String,
    val user: String,
    val password: String,
)
@Serializable
data class HomeserverConfig(
    val bindHost: String,
    val bindPort: Int,
    val db: DBConfig,
) {
    companion object {
        private lateinit var instance: HomeserverConfig
        const val DEFAULT_CONFIG_LOCATION_LINUX = "/etc/friendgroup/config.json"

        fun load(file: File): HomeserverConfig {
            val conf: HomeserverConfig = Json.decodeFromString(file.readText())
            instance = conf
            return conf
        }

        val INSTANCE get() = instance
    }
}