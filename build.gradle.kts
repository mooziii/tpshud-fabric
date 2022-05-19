plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    id("fabric-loom") version "0.12-SNAPSHOT"
    java
}

group = "me.obsilabor"
version = "1.3-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com")
}

dependencies {
    // kotlin
    implementation(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    // event system
    implementation("me.obsilabor:alert:1.0.5")
    include("me.obsilabor:alert:1.0.5")
    // fabric
    minecraft("com.mojang:minecraft:1.19-pre1")
    mappings("net.fabricmc:yarn:1.19-pre1+build.1")
    modImplementation("net.fabricmc:fabric-loader:0.14.5")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.7.4+kotlin.1.6.21")
    // modmenu & clothconfig
    modApi("com.terraformersmc:modmenu:3.1.0")
    modApi("me.shedaniel.cloth:cloth-config-fabric:7.0.61") {
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