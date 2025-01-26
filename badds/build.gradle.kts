plugins {
    id("buildlogic.java-application-conventions")
    id("nl.colorize.gradle.application") version "2024.4"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

buildscript {
    repositories {
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.openjfx:javafx-plugin:0.1.0")
    }
}
apply(plugin = "org.openjfx.javafxplugin")

repositories {
    mavenCentral()
}

javafx {
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":shared"))
    implementation("org.httprpc:sierra:2.2")
    implementation("com.formdev:flatlaf:3.4.1")
    testImplementation("junit:junit:4.13.1")
}
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "org.dds_utils.badds.App"
    }
}

application {
    mainClass = "org.dds_utils.badds.App"
}

