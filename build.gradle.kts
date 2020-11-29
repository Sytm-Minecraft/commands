import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20"
    id("org.jetbrains.dokka") version "1.4.10.2"
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "de.md5lukas"
version = "1.0.0-SNAPSHOT"
description = "Command DSL for spigot"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()

        maven(url = "https://repo.sytm.de/repository/maven-hosted/")
        maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven(url = "https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    implementation("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    implementation("de.md5lukas:md5-commons:2.0.0-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:20.1.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    minimize()

    dependencies {
        include(dependency("de.md5lukas:md5-commons"))
    }

    relocate("de.md5lukas.commons", "de.md5lukas.commands.internal.commons")
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    dependsOn(tasks.dokkaJavadoc)
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

publishing {
    repositories {
        maven {
            val releasesRepoUrl = "https://repo.sytm.de/repository/maven-releases/"
            val snapshotsRepoUrl = "https://repo.sytm.de/repository/maven-snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                if (project.hasProperty("mavenUsername")) {
                    username = project.properties["mavenUsername"] as String
                }
                if (project.hasProperty("mavenPassword")) {
                    password = project.properties["mavenPassword"] as String
                }
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            project.shadow.component(this)
            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }
}
