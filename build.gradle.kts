import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options

plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.21"
    id("fabric-loom") version "1.4-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.+"
    id("com.matthewprenger.cursegradle") version "1.4.0"
    id("java")
}

group = "me.obsilabor"
version = "1.7.0+1.20.3"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.isxander.dev/releases")
    maven("https://api.modrinth.com/maven")
}

dependencies {
    // kotlin
    implementation(kotlin("stdlib"))
    // event system
    implementation("me.obsilabor:alert:1.0.8")
    include("me.obsilabor:alert:1.0.8")
    // paper
    /**
     * I'm not using userdev here because I think it would cause issues together with loom.
     * Additionally, I don't use any NMS specific functions or classes
     */
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    // fabric
    minecraft("com.mojang:minecraft:1.20.3")
    mappings("net.fabricmc:yarn:1.20.3+build.1")
    modImplementation("net.fabricmc:fabric-loader:0.15.0")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.91.1+1.20.3")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.16+kotlin.1.9.21")
    // modmenu
    modApi("maven.modrinth:modmenu:9.0.0-pre.1")
    // yacl
    modImplementation("dev.isxander.yacl:yet-another-config-lib-fabric:3.3.0-beta.1+1.20.3")
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
    projectId.set("tps-hud")
    versionNumber.set(project.version.toString())
    versionType.set("release")
    gameVersions.addAll(listOf("1.20.2"))
    loaders.add("fabric")
    loaders.add("quilt")
    loaders.add("purpur")
    loaders.add("paper")
    loaders.add("spigot")
    loaders.add("bukkit")
    dependencies {
        required.project("fabric-api")
        required.project("fabric-language-kotlin")
        optional.project("yacl")
        optional.project("modmenu")
    }
    uploadFile.set(tasks.remapJar.get())
}

curseforge {
    project(closureOf<CurseProject> {
        apiKey = System.getenv("CURSEFORGE_TOKEN")
        mainArtifact(tasks.remapJar.get())

        id = "610618"
        releaseType = "release"
        addGameVersion("1.20.2")
        addGameVersion("Fabric")
        addGameVersion("Quilt")

        relations(closureOf<CurseRelation> {
            requiredDependency("fabric-api")
            requiredDependency("fabric-language-kotlin")
            optionalDependency("yacl")
            optionalDependency("modmenu")
        })
    })
    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}
