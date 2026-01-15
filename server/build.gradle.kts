import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    java
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
}

allprojects {
    group = "com.sharetimer"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    val libs = rootProject.libs

    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
        }
    }

    configure<DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:${libs.versions.springBoot.get()}")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}")
        }
    }

    dependencies {
        compileOnly(libs.lombok)
        annotationProcessor(libs.lombok)
        testCompileOnly(libs.lombok)
        testAnnotationProcessor(libs.lombok)

        annotationProcessor(libs.spring.boot.configuration.processor)

        testImplementation(libs.spring.boot.starter.test)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
    }

    if (path.startsWith(":apps")) {
        apply(plugin = "org.springframework.boot")

        dependencies {
            add("developmentOnly", libs.spring.boot.devtools)
        }
    }

    if (path.startsWith(":libs")) {
        tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
            enabled = false
        }
        tasks.withType<Jar> {
            enabled = true
        }
    }
}
