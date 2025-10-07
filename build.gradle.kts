plugins {
    java
	id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "io.github.darvil82"
version = "2.2.0"
description = "Text formatting utilities to easily format text on the terminal for Java."

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	mavenLocal()
}

dependencies {
    implementation("io.github.darvil82:utils:0.7.1")

    implementation("org.jetbrains:annotations:24.0.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

mavenPublishing {
	publishToMavenCentral()
	signAllPublications()
	coordinates(group.toString(), name.toString(), version.toString())

	pom {
		name.set("Terminal Text Formatter")
		description.set(project.description)
		inceptionYear.set("2023")
		url.set("https://github.com/darvil82/java-terminal-text-formatter")
		licenses {
			license {
				name.set("MIT License")
				url.set("https://opensource.org/license/mit")
				distribution.set("https://opensource.org/license/mit")
			}
		}
		developers {
			developer {
				id.set("darvil82")
				name.set("darvil82")
				url.set("https://github.com/darvil82/")
			}
		}
		scm {
			url.set("https://github.com/darvil82/java-terminal-text-formatter")
			connection.set("scm:git:git://github.com/darvil82/java-terminal-text-formatter.git")
			developerConnection.set("scm:git:ssh://git@github.com/darvil82/java-terminal-text-formatter.git")
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