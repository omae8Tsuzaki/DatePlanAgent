plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.dateplan"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.dateplan.DatePlanAgentApp")
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")

    // Discord
    implementation("net.dv8tion:JDA:6.3.2")

    // OpenAI
    implementation("com.openai:openai-java:4.29.0")

    // JSON
    implementation("com.google.code.gson:gson:2.13.2")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
