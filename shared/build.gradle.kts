plugins {
    id("buildlogic.java-application-conventions")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.activation:activation:1.1.1")
    implementation("io.github.memo33:jsquish:2.1.0")
    implementation("org.jogamp.gluegen:gluegen-rt-main:2.3.2")
    implementation("org.jogamp.jogl:jogl-all-main:2.3.2")
    testImplementation("junit:junit:4.13")
}