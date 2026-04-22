plugins {
    kotlin("jvm") version "1.9.22"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Pact for contract testing
    testImplementation("au.com.dius.pact.consumer:junit5:4.6.3")
    // RestAssured for API validation
    testImplementation("io.rest-assured:rest-assured:5.3.2")
    // Kotlin test library
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    // Setting for Pact files generation directory
    systemProperty("pact.rootDir", "${projectDir}/build/pacts")
}
