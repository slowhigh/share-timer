plugins {
    `java-library`
}

dependencies {
    api(libs.spring.boot.autoconfigure)
    api(libs.spring.data.redis)
    api(libs.lettuce.core)
}
