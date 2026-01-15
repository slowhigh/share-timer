import org.springframework.boot.gradle.tasks.run.BootRun

dependencies {
    implementation(project(":libs:common"))
    implementation(project(":libs:web-support"))
    implementation(project(":libs:db-jpa"))
    implementation(project(":libs:storage-redis"))
    
    implementation(libs.bundles.observability)
    implementation(libs.bundles.eureka.client)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
    
    runtimeOnly(libs.h2)
    runtimeOnly(libs.postgresql)
}
