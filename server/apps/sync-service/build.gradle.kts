import org.springframework.boot.gradle.tasks.run.BootRun

dependencies {
    implementation(project(":libs:core"))
    implementation(project(":libs:web-support")) {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-web")
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.named<BootRun>("bootRun") {
    systemProperty("spring.profiles.active", findProperty("profile") ?: "local")
}
