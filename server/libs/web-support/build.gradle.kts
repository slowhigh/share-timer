plugins {
    `java-library`
}

dependencies {
    implementation(project(":libs:common"))
    
    api(libs.spring.web)
}
