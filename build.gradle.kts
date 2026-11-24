import gg.meza.stonecraft.mod

plugins {
    id("gg.meza.stonecraft")
    id("com.hypherionmc.modutils.modpublisher") version "2.2.4"
    `maven-publish`
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://maven.architectury.dev")
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.firstdark.dev/releases")
}

val generatedDir = file("src/main/generated")

sourceSets {
    main {
        resources {
            srcDir(generatedDir)
        }
    }
}

loom {
    version = project.mod.version
    enableTransitiveAccessWideners = false
    runs.named("client") {
        programArgs("--uuid", "00000000-0000-0000-0000-000000000000")
    }
}

val modVersion = project.version.toString()
val mcVersion2 = stonecutter.current.version
val loaderName = stonecutter.current.project.substringAfterLast('-', stonecutter.current.project)

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks.named("jar"))
            groupId = "com.github.starcatcher21.starfarm"
            artifactId = "${modVersion}-${mcVersion2}"
            version = loaderName
        }
    }
}

modSettings {
    clientOptions {
        fov = 110
        guiScale = 3
        narrator = false
        darkBackground = true
        musicVolume = 0.0
    }

    variableReplacements =
        mapOf(
            "minecraftVersionVirtual" to stonecutter.current.version,
            "forgeLoaderVersion" to
                    if (project.mod.isForge) {
                        project.mod
                            .prop("forge_version")
                            .substringAfter("-")
                            .substringBefore(".")
                    } else {
                        ""
                    },
        )
}

java {
    val javaVersion = when {
        stonecutter.current.project.startsWith("26.1") -> 25
        stonecutter.current.project.startsWith("26.1.1") -> 25
        stonecutter.current.project.startsWith("26.1.2") -> 25
        stonecutter.current.project.startsWith("26.2") -> 25
        stonecutter.current.project.startsWith("26.3") -> 25
        else -> 25
    }
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
}


dependencies {
    val archVer = project.mod.prop("architectury_api_version")

    when {
        project.mod.isFabric -> {
            implementation("dev.architectury:architectury:${archVer}")
            api("dev.architectury:architectury-fabric:${archVer}")
            implementation("com.github.starcatcher21.starlib:${project.mod.prop("starlib_version")}-${stonecutter.current.version}:fabric@jar")
            api("teamreborn:energy:${project.mod.prop("energy")}")
        }
        stonecutter.current.project.endsWith("-neoforge") -> {
            implementation("dev.architectury:architectury-neoforge:$archVer") {
                exclude(group = "dev.architectury", module = "architectury")
            }
            implementation("com.github.starcatcher21.starlib:${project.mod.prop("starlib_version")}-${stonecutter.current.version}:neoforge@jar")
        }
    }
}

val loader = project.name.substringAfterLast('-')
val mcVersion = project.name.substringBeforeLast('-')

tasks.named("publishMod") {
    dependsOn(if (tasks.names.contains("remapJar")) "remapJar" else "jar")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

val dotenv: Map<String, String> = rootProject.file(".env")
    .takeIf { it.exists() }
    ?.readLines()
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() && !it.startsWith("#") && "=" in it }
    ?.associate {
        it.substringBefore("=").trim() to
                it.substringAfter("=").trim().removeSurrounding("\"")
    }
    ?: emptyMap()

fun secret(name: String): String? =
    providers.environmentVariable(name).orNull ?: dotenv[name]

publisher {
    apiKeys {
        modrinth(secret("MODRINTH_TOKEN"))
        curseforge(secret("CURSE_TOKEN"))
    }

    modrinthID.set("JEPPg7D9")
    curseID.set("1824280")


    versionType.set("release")
    projectVersion.set("${property("mod.version")}+$mcVersion-$loader")
    displayName.set("StarFarm ${property("mod.version")} [$loader $mcVersion]")

    if (loader == "fabric") {
        modrinthDepends { required("fabric-api") }
        curseDepends { required("fabric-api") }
    }
    modrinthDepends {
        required("architectury-api")
        required("starlib")
    }
    curseDepends {
        required("architectury-api")
        required("starlibb")
    }

    gameVersions.set(listOf(mcVersion))
    changelog.set(rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText() ?: "No changelog provided.")
    loaders.set(listOf(loader))
    curseEnvironment.set("both")

    artifact.set(
        if (tasks.names.contains("remapJar")) tasks.named("remapJar") else tasks.named("jar")
    )
}
