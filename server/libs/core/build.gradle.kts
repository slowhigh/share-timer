plugins {
    `java-library`
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-data-redis")
    api("org.springframework:spring-web")
    api("com.fasterxml.jackson.core:jackson-annotations")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("io.swagger.core.v3:swagger-annotations:2.2.22")
}
