plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    signing

    alias(libs.plugins.nexus.publish)
}

group = "dev.httpmarco.polocloud"
version = "3.0.0-pre.8-SNAPSHOT"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.gson)
}

gradlePlugin {
    plugins {
        create("polocloudModule") {
            id = "dev.httpmarco.polocloud.module"
            implementationClass = "dev.httpmarco.polocloud.module.gradle.PolocloudModulePlugin"
            displayName = "PoloCloud Module Gradle Plugin"
            description = "Gradle plugin for building PoloCloud modules"
        }
    }
}

publishing {
    publications {
        withType<MavenPublication>().configureEach {
            pom {
                name.set("PoloCloud Module Gradle Plugin")
                description.set("Gradle plugin for PoloCloud modules")
                url.set("https://github.com/thePolocloud/polocloud")

                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }

                developers {
                    developer {
                        id.set("httpmarco")
                        name.set("Mirco Lindenau")
                        email.set("mirco.lindenau@gmx.de")
                    }
                }

                scm {
                    url.set("https://github.com/thePolocloud/polocloud")
                    connection.set("scm:git:https://github.com/thePolocloud/polocloud.git")
                    developerConnection.set("scm:git:https://github.com/thePolocloud/polocloud.git")
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

            username.set(System.getenv("OSSRH_USERNAME"))
            password.set(System.getenv("OSSRH_PASSWORD"))
        }
    }
}

signing {
    val signingKey = System.getenv("GPG_PRIVATE_KEY")
    val signingPassphrase = System.getenv("GPG_PASSPHRASE")

    if (signingKey != null && signingPassphrase != null) {
        useInMemoryPgpKeys(signingKey, signingPassphrase)
        sign(publishing.publications)
    }
}