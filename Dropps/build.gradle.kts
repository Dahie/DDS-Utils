plugins {
    id("buildlogic.java-application-conventions")
    id("nl.colorize.gradle.application") version "2025.1.2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation("org.httprpc:sierra:2.8")
    implementation("com.formdev:flatlaf:3.4.1")
    //testImplementation("junit:junit:4.13.1")
}

apply<JavaPlugin>()
apply<ApplicationPlugin>()

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    sourceSets["main"].java.srcDirs("source")
    sourceSets["main"].resources.srcDirs("../resources")
    //sourceSets["test"].java.srcDirs("source")
}

macApplicationBundle {
    name = "Dropps"
    identifier = "org.dds_utils.dropps"
    description = "A description for your application"
    copyright = "Copyright 2025"
    bundleVersion = "1.4"
    icon = "resources/Icon.icns"
    applicationCategory = "public.app-category.developer-tools"
    mainClassName = "org.dds_utils.dropps.Dropps"
    val output: Provider<Directory> = layout.buildDirectory.dir("fatJar")
    contentDir = output.get().asFile.path
}

application {
    mainClass = "org.dds_utils.dropps.Dropps"
}


//exe {
//    inherit = true
//    mainJarName = "example.jar"
//    icon = "${projectDir}/../resources/icon.ico"
//    supportURL = "https://www.colorize.nl"
//}

//tasks.register<Jar>("fatJar") {
//    destinationDirectory.set(layout.buildDirectory.dir("fatJar"))
//    archiveFileName.set("example-fat.jar")
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//    exclude("**/module-info.class")
//    exclude("**/META-INF/INDEX.LIST")
//    exclude("**/META-INF/*.SF")
//    exclude("**/META-INF/*.DSA")
//    exclude("**/META-INF/*.RSA")
//
//    manifest {
//        attributes("Application-Name" to "Dropps")
//        attributes("Main-Class" to "org.dds_utils.dropps.Dropps")
//    }
//
//    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
//    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
//
//    with(tasks.getByName("jar") as Copy)
//}
//
//tasks.named("createApplicationBundle").configure {
//    dependsOn("fatJar")
//}
