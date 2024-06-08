plugins {
    id("buildlogic.java-application-conventions")
    id("nl.colorize.gradle.application") version "2024.4"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation("org.httprpc:sierra:2.2")
    implementation("com.formdev:flatlaf:3.4.1")
    testImplementation("junit:junit:4.13.1")
}
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "org.dds_utils.dropps.Dropps"
    }
}
//
//configure<macApplicationBundle> {
//    name = "Dropps"
//    identifier = "org.dds_utils.dropps"
//    description = "A description for your application"
//    copyright = "Copyright 2024"
//    bundleVersion = "1.0"
//    icon = "resources/icon.icns"
//    applicationCategory = "public.app-category.developer-tools"
//    mainClassName = "com.example.app.Main"
//    outputDir = "${buildDir}"
//    options = ["-Xmx2g"]
//    startOnFirstThread = false
//}

application {
    mainClass = "org.dds_utils.dropps.Dropps"
}

