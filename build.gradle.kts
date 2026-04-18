plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.dlucxoket"
version = "0.0.1-SNAPSHOT"
description = "LucatBot"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    /** Spring Boot 核心 starter：提供 Spring Boot 基础功能 */
    implementation("org.springframework.boot:spring-boot-starter")
    
    /** Kotlin 反射库：支持 Kotlin 反射特性 */
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    
    /** Shiro OneBot 框架：QQ 机器人开发框架 */
    implementation("com.mikuac:shiro:2.5.0")
    
    /** Spring Boot 测试框架：提供 Spring Boot 应用测试支持 */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    
    /** Kotlin Test JUnit5：Kotlin 语言的 JUnit5 测试支持 */
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    
    /** JUnit Platform Launcher：JUnit5 测试运行器 */
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
