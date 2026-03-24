dependencies {
    implementation(project(":libs:common"))
    implementation(project(":libs:web-support"))
    implementation(project(":libs:storage-redis"))

    implementation(libs.bundles.observability)
    implementation(libs.bundles.eureka.client)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.springdoc.openapi.starter.webflux.ui)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)
}
