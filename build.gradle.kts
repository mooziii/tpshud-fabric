import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.+"
    id("com.matthewprenger.cursegradle") version "1.4.0"
    java
}

group = "me.obsilabor"
version = "1.4.2+1.19.1"

repositories {
    mavenCentral()
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com")
}

dependencies {
    // kotlin
    implementation(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0-RC")
    // event system
    implementation("me.obsilabor:alert:1.0.6")
    include("me.obsilabor:alert:1.0.6")
    // fabric
    minecraft("com.mojang:minecraft:1.19.1")
    mappings("net.fabricmc:yarn:1.19.1+build.1")
    modImplementation("net.fabricmc:fabric-loader:0.14.8")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.8.2+kotlin.1.7.10")
    // modmenu & clothconfig
    modApi("com.terraformersmc:modmenu:4.0.5")
    modApi("me.shedaniel.cloth:cloth-config-fabric:7.0.73") {
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
    gameVersions.addAll(listOf("1.19.1"))
    loaders.add("fabric")
    loaders.add("quilt")
    dependencies {
        required.project("Ha28R6CL")
        optional.project("mOgUt4GM")
        required.project("9s6osm5g")
    }

    uploadFile.set(tasks.remapJar.get())
}

curseforge {
    project(closureOf<CurseProject> {
        apiKey = System.getenv("CURSEFORGE_TOKEN")
        mainArtifact(tasks.remapJar.get())

        id = "610618"
        releaseType = "release"
        addGameVersion("1.19.1")
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