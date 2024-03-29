import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
	kotlin("plugin.jpa") version "1.9.22"
	kotlin("kapt") version "1.8.22"
}

group = "com.teamsparta"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

val queryDslVersion = "5.0.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	// jwt
	implementation("io.jsonwebtoken:jjwt-api:0.12.3")
	//swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	// validation
	implementation("org.springframework.boot:spring-boot-starter-validation")
	// oauth2
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	//jwt
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	//Querydsl
	implementation("com.querydsl:querydsl-jpa:$queryDslVersion:jakarta")
	kapt("com.querydsl:querydsl-apt:$queryDslVersion:jakarta")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
