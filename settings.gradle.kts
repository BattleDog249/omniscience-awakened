pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "FabricMC" }
        maven("https://maven.kikugie.dev/releases/") { name = "KikuGie Releases" }
    }
}

plugins {
    // Check the latest version on https://stonecutter.kikugie.dev/blog/changes/0.10
    id("dev.kikugie.stonecutter") version "0.10"

    // Used for cross-compat for 26.1+ and older versions (https://codeberg.org/KikuGie/loom-back-compat)
    id("dev.kikugie.loom-back-compat") version "0.4.3"

    // Sometimes it is needed to make Gradle run at all, so it doesn't hurt to have
    // (https://github.com/gradle/foojay-toolchains)
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        // See https://stonecutter.kikugie.dev/wiki/v2/guides/tools/minecraft-changes
        versions("26.1", "26.2", "26.3")
    }
}

gradle.lifecycle.beforeProject {
    repositories {
        /**
         * Restricts dependency search of the given [groups] to the [maven URL][url],
         * improving the setup speed.
         */
        fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
            forRepository { maven(url) { name = alias } }
            filter { groups.forEach(::includeGroup) }
        }
        strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
        strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")

        // Cloth Config
        strictMaven("https://maven.shedaniel.me/", "Shedaniel", "me.shedaniel.cloth")

        // Mod Menu
        strictMaven("https://maven.terraformersmc.com/", "Terraformers", "com.terraformersmc")
    }
}

rootProject.name = "Omniscience Awakened"
