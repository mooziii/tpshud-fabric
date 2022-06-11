import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.+"
    id("com.matthewprenger.cursegradle") version "1.4.0"
    java
}

group = "me.obsilabor"
version = "1.4+1.19"

repositories {
    mavenCentral()
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com")
}

dependencies {
    // kotlin
    implementation(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    // event system
    implementation("me.obsilabor:alert:1.0.6")
    include("me.obsilabor:alert:1.0.6")
    // fabric
    minecraft("com.mojang:minecraft:1.19")
    mappings("net.fabricmc:yarn:1.19+build.1")
    modImplementation("net.fabricmc:fabric-loader:0.14.6")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.7.4+kotlin.1.6.21")
    // modmenu & clothconfig
    modApi("com.terraformersmc:modmenu:4.0.0")
    modApi("me.shedaniel.cloth:cloth-config-fabric:7.0.65") {
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
    named("curseforge") {
        onlyIf {
            System.getenv("CURSEFORGE_TOKEN") != null
        }
        dependsOn(remapJar)
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("2UeET9aA")
    versionNumber.set(project.version.toString())
    versionType.set("release")
    gameVersions.addAll(listOf("1.19"))
    loaders.add("fabric")
    loaders.add("quilt")
    dependencies {
        required.project("Ha28R6CL")
        optional.project("mOgUt4GM")
    }

    uploadFile.set(tasks.remapJar.get())
}

curseforge {
    project(closureOf<CurseProject> {
        apiKey = System.getenv("CURSEFORGE_TOKEN")
        mainArtifact(tasks.remapJar.get())

        id = "610618"
        releaseType = "release"
        addGameVersion("1.19")
        addGameVersion("Java 17")
        addGameVersion("Fabric")
        addGameVersion("Quilt")

        relations(closureOf<CurseRelation> {
            requiredDependency("cloth-config")
            requiredDependency("fabric-language-kotlin")
        })
    })
    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}