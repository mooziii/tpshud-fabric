plugins {
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.serialization") version "1.6.20"
    id("fabric-loom") version "0.12-SNAPSHOT"
    java
}

group = "me.obsilabor"
version = "1.2-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("me.obsilabor:alert:1.0.1")
    include("me.obsilabor:alert:1.0.1")
    minecraft("com.mojang:minecraft:1.18.2")
    mappings("net.fabricmc:yarn:1.18.2+build.2")
    modImplementation("net.fabricmc:fabric-loader:0.13.3")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.7.2+kotlin.1.6.20")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    modApi("com.terraformersmc:modmenu:3.1.0")
    modApi("me.shedaniel.cloth:cloth-config-fabric:6.2.57") {
        exclude("net.fabricmc.fabric-api")
    }
}

tasks {
    processResources {
        val properties = mapOf(
            "version" to project.version,
        )
        inputs.properties(properties)
        filesMatching("fabric.mod.json") {
            expand(properties)
        }
    }
}