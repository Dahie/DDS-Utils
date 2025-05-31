plugins {
    id("buildlogic.java-application-conventions")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.memo33:jsquish:2.1.0")
    implementation("org.jogamp.gluegen:gluegen-rt-main:2.3.2")
    implementation("org.jogamp.jogl:jogl-all-main:2.3.2")
    testImplementation("junit:junit:4.13")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStackTraces = true
    }
}