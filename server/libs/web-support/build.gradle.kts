plugins {
    `java-library`
}

dependencies {
    api(project(":libs:core"))
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("io.swagger.core.v3:swagger-annotations:2.2.22")
}
