plugins {
    `java-library`
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    api(libs.spring.boot.starter.data.jpa)
}
