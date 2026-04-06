plugins {
    id("java")
    id("application")
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
    implementation(libs.spring.boot.starter)
    implementation(platform(libs.spring.boot.bom))
    testImplementation(libs.spring.boot.starter.test)

    // Discord
    implementation(libs.jda)

    // OpenAI
    implementation(libs.openai)

    // Gson
    implementation(libs.gson)

    // Test
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
