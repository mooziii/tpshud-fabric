import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.+"
    id("com.matthewprenger.cursegradle") version "1.4.0"
    id("java")
}

group = "me.obsilabor"
version = "1.5.1+1.19.2"

repositories {
    mavenCentral()
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // kotlin
    implementation(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0-RC")
    // event system
    implementation("me.obsilabor:alert:1.0.6")
    include("me.obsilabor:alert:1.0.6")
    // paper
    /**
     * I'm not using userdev here because I think it would cause issues together with loom.
     * Additionally, I don't use any NMS specific functions or classes
     */
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    // fabric
    minecraft("com.mojang:minecraft:1.19.2")
    mappings("net.fabricmc:yarn:1.19.2+build.1")
    modImplementation("net.fabricmc:fabric-loader:0.14.9")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.8.2+kotlin.1.7.10")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.59.0+1.19.2")
    // modmenu & cloth-config
    modApi("net.silkmc:silk-core:1.9.0")
    modApi("com.terraformersmc:modmenu:4.0.6") {
        exclude("net.fabricmc.fabric-api")
    }
    modApi("me.shedaniel.cloth:cloth-config-fabric:8.0.75") {
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
        filesMatching("plugin.yml") {
            expand(properties)
        }
    }
    named("curseforge") {
        onlyIf {
            System.getenv("CURSEFORGE_TOKEN") != null
        }
        dependsOn(remapJar)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("2UeET9aA")
    versionNumber.set(project.version.toString())
    versionType.set("release")
    gameVersions.addAll(listOf("1.19", "1.19.1", "1.19.2"))
    loaders.add("fabric")
    loaders.add("quilt")
    dependencies {
        required.project("P7dR8mSH") // fabric-api
        required.project("Ha28R6CL") // fabric-language-kotlin
        optional.project("9s6osm5g") // cloth-config
        optional.project("mOgUt4GM") // modmenu
        optional.project("aTaCgKLW") // silk (only required on the server side)
    }
    uploadFile.set(tasks.remapJar.get())
}

curseforge {
    project(closureOf<CurseProject> {
        apiKey = System.getenv("CURSEFORGE_TOKEN")
        mainArtifact(tasks.remapJar.get())

        id = "610618"
        releaseType = "release"
        addGameVersion("1.19.2")
        addGameVersion("1.19.1")
        addGameVersion("1.19")
        addGameVersion("Fabric")
        addGameVersion("Quilt")

        relations(closureOf<CurseRelation> {
            requiredDependency("fabric-api")
            requiredDependency("fabric-language-kotlin")
            optionalDependency("cloth-config")
            optionalDependency("modmenu")
            optionalDependency("silk-kt")
        })
    })
    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}
