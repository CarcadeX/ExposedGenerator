import java.net.URI

plugins {
    kotlin("jvm") version "1.8.21"
    id("com.google.devtools.ksp") version "1.8.21-1.0.11"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("maven-publish")
    id("signing")
    id("io.codearte.nexus-staging") version "0.30.0"
}

val globalArtifactId = "exposedgenerator"
val globalName = "ExposedGenerator"
group = "me.redtea"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
  //  ksp(project(":processor"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.21-1.0.11")
    implementation("com.squareup:kotlinpoet:1.10.1")
    implementation("com.squareup:kotlinpoet-ksp:1.10.1")
    implementation("org.jetbrains.exposed:exposed-core:0.40.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.40.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}

tasks {
    shadowJar {
        dependencies {
            exclude(dependency("org.jetbrains.exposed:exposed-core:0.40.1"))
            exclude(dependency("org.jetbrains.exposed:exposed-dao:0.40.1"))
            exclude(dependency("org.jetbrains.exposed:exposed-core:0.40.1"))
        }
    }
}


java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = globalArtifactId
            groupId = "io.github.iredtea"
            version = version
            from(components["java"])
                    versionMapping {
                        usage("java-api") {
                            fromResolutionOf("runtimeClasspath")
                        }
                        usage("java-runtime") {
                            fromResolutionResult()
                        }
                    }
            pom {
                artifactId = globalArtifactId
                groupId = "io.github.iredtea"
                version = version
                name.set(globalArtifactId)
                description.set(globalArtifactId)
                url.set("https://github.com/iRedTea/CarcadeX")

                licenses {
                    license {
                        name.set("GNU General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
                    }
                }
                developers {
                    developer {
                        id.set("itzRedTea")
                        name.set("Red Tea")
                        email.set("red__tea@outlook.com")
                    }
                }
                scm {
                    connection.set("scm:https://github.com/CarcadeX/$globalName.git")
                    developerConnection.set("git@github.com:CarcadeX/$globalName.git")
                    url.set("https://github.com/iRedTea/CarcadeX")
                }
            }

        }
    }



    repositories {
        maven {
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = URI(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = "itzRedTea"
                password = project.findProperty("password").toString()
            }
        }

    }
}




nexusStaging {
    serverUrl = "https://s01.oss.sonatype.org/service/local/"
    packageGroup = "io.github.iredtea"
    username = "itzRedTea"
    password = project.findProperty("password").toString()

}


signing {
    useGpgCmd()
    sign(publishing.publications.getByName("maven"))
}

/*

javadoc {
    if(JavaVersion.current().isJava9Compatible()) {
        options.addBooleanOption('html5', true)
    }
}

artifacts {
    archives javadocJar, sourcesJar
}

 */