import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.jpa) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
    alias(libs.plugins.spotless)
}

allprojects {
    group = "com.sharetimer"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

spotless {
    val ktlintVersion = libs.versions.ktlint.get()
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt", "**/bin/**/*.kt")
        ktlint(ktlintVersion)
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint(ktlintVersion)
    }
}

subprojects {
    val libs = rootProject.libs

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "io.spring.dependency-management")

    extensions.configure<JavaPluginExtension> {
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
        add("implementation", libs.kotlin.reflect)
        add("implementation", libs.jackson.module.kotlin)

        add("annotationProcessor", libs.spring.boot.configuration.processor)

        add("testImplementation", libs.spring.boot.starter.test)
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.addAll("-Xjsr305=strict", "-java-parameters")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    if (path.startsWith(":apps:")) {
        apply(plugin = "org.springframework.boot")

        dependencies {
            add("developmentOnly", libs.spring.boot.devtools)
        }
    }

    if (path.startsWith(":libs:")) {
        tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
            enabled = false
        }
        tasks.withType<Jar> {
            enabled = true
        }
    }
}
