plugins {
    `java-library`
}

dependencies {
    api(libs.spring.boot.starter.jdbc)
    api(libs.spring.boot.starter.data.jdbc)
    api(libs.spring.boot.starter.data.jpa)
}
