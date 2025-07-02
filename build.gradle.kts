plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	application
	jacoco
	id("info.solidsoft.pitest") version "1.19.0-rc.1"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
	testImplementation("io.kotest:kotest-assertions-core:5.9.1")
	testImplementation("io.kotest:kotest-property:5.9.1")
	testImplementation("io.mockk:mockk:1.13.7")
	testImplementation("com.ninja-squad:springmockk:4.0.0")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.liquibase:liquibase-core")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.testcontainers:junit-jupiter:1.19.1")
	testImplementation("org.testcontainers:postgresql:1.19.1")
	testImplementation("org.testcontainers:jdbc-test:1.12.0")
	testImplementation("org.testcontainers:testcontainers:1.19.1")
	testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

pitest {
	junit5PluginVersion.set("1.2.1")
	targetClasses.set(listOf("Methodo_test.domain.model.*"))
	targetTests.set(listOf("Methodo_test.usecase.*"))
	threads.set(2)
	outputFormats.set(listOf("HTML"))
}

jacoco {
	toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test, tasks.named("testIntegration")) // important

	reports {
		xml.required.set(true)
		html.required.set(true)
	}

	// Ajouter les résultats des tests d'intégration
	executionData.setFrom(
		fileTree(layout.buildDirectory).include(
			"/jacoco/test.exec",
			"/jacoco/testIntegration.exec"
		)
	)
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

sourceSets {
	create("testIntegration") {
		java.srcDir("src/testIntegration/kotlin")
		resources.srcDir("src/testIntegration/resources")
		compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
		runtimeClasspath += output + compileClasspath
	}
}

tasks.register<Test>("testIntegration") {
	description = "Run integration tests"
	group = "verification"
	testClassesDirs = sourceSets["testIntegration"].output.classesDirs
	classpath = sourceSets["testIntegration"].runtimeClasspath
	useJUnitPlatform()
}

tasks.named("check") {
	dependsOn("testIntegration")
}
