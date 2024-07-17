plugins {
    kotlin("jvm") version "2.0.0"
    `maven-publish`
}

group = "io.github.kituin"
version = "1.3.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}
tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}
tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks["javadoc"])
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.name
            groupId = project.group.toString()
            version = project.version.toString()

            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name.set(project.name)
                description.set("Interpreter for ModMultiVersion")
                url.set("https://github.com/kitUIN/ModMultiVersionInterpreter")

                licenses {
                    license {
                        name.set("MIT")
                        url.set("")
                    }
                }

                developers {
                    developer {
                        id.set("kitUIN")
                        name.set("kitUIN")
                        email.set("KIT_UIN@outlook.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/kitUIN/ModMultiVersionInterpreter.git")
                    developerConnection.set("scm:git:ssh://github.com/kitUIN/ModMultiVersionInterpreter.git")
                    url.set("https://github.com/kitUIN/ModMultiVersionInterpreter")
                }
            }
        }
    }

    repositories {
        maven {
            name = project.name
            url = uri(if (project.version.toString().endsWith("-SNAPSHOT")) {
                "https://maven.kituin.fun/snapshots"
            } else {
                "https://maven.kituin.fun/releases"
            })

            credentials {
                username = findProperty("user") as String? ?: System.getenv("KITUIN_USERNAME")
                password = findProperty("pwd") as String? ?: System.getenv("KITUIN_PASSWORD")
            }
        }
    }
}
