plugins {
    kotlin("jvm") version "1.9.21"
}

group = "sh.illumi.friendgroup"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}