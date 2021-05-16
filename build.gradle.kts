import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.5.0"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}



group = "me.cowin.job"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    testImplementation(kotlin("test-junit"))
    implementation("io.ktor:ktor-client-core:1.5.4")
    implementation("io.ktor:ktor-client-cio:1.5.4")
    implementation("io.ktor:ktor-client-serialization:1.5.4")
    implementation("io.ktor:ktor-client-logging:1.5.4")
    implementation("com.google.firebase:firebase-admin:7.2.0")

}

tasks.test {
    useJUnit()
}
tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.cowin.MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }

}

tasks.withType<ShadowJar>() {
    manifest {
        attributes["Main-Class"] = "com.cowin.MainKt"
    }
}


tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs =  kotlinOptions.freeCompilerArgs + "-Xallow-result-return-type"
}

