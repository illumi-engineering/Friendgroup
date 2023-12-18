pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0" apply false
    kotlin("jvm") version "1.9.21" apply false
    kotlin("plugin.serialization") version "1.9.21" apply false
    id("io.ktor.plugin") version "2.3.7" apply false
}

rootProject.name = "Friendgroup"
include(":homeserver")
