plugins {
    java
    `maven-publish`
}

group = "com.darvil"
version = "2.2.0"
description = "Text formatting utilities to easily format text on the terminal for Java."

dependencies {
    implementation("com.darvil:utils:0.6.1b")

    implementation("org.jetbrains:annotations:24.0.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    withJavadocJar()
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://api.repsy.io/mvn/darvil/java")
}

publishing {
    repositories {
        maven {
            name = "Github"
            url = uri("https://maven.pkg.github.com/darvil82/java-terminal-text-formatter")
            credentials(PasswordCredentials::class)
        }

        maven {
            name = "Repsy"
            url = uri("https://api.repsy.io/mvn/darvil/java")
            credentials(PasswordCredentials::class)
        }

        mavenLocal()
    }

    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}