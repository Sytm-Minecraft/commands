import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "de.md5lukas"
version = "1.0.0-SNAPSHOT"

dependencies {
    api(kotlin("stdlib-jdk8"))
    implementation("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    implementation(project(path = ":", configuration = "shadow"))
}

tasks.withType<ProcessResources> {
    outputs.upToDateWhen { false }
    expand("version" to project.version)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")

    dependencies {
        include(dependency("de.md5lukas:commands"))
    }
}