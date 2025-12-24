# Libs 리팩토링 및 의존성 정리 계획

## Goal Description
현재 `libs/core`는 도메인, 인프라(Redis, JPA), 공통 예외 등 여러 관심사가 섞여 있으며, `spring-web`, `data-redis`, `data-jpa` 등 무거운 의존성을 모두 가지고 있습니다.
이로 인해 `libs/web-support`와의 경계가 모호하고, 불필요한 의존성이 전파되는 문제가 있습니다.
이를 해결하기 위해 역할별로 라이브러리를 분리하여 의존성을 명확히 하고 가볍게 만듭니다.

## User Review Required
> [!WARNING]
> 이 변경은 파일 이동 및 패키지 변경을 포함하므로, 이 라이브러리를 사용하는 모든 애플리케이션(`apps/*`)의 `build.gradle.kts` 및 `import` 구문 수정이 필요합니다.

## Proposed Changes

### 1. New Library: `libs/common` (Pure Java/Kotlin Shared)
가장 기본이 되는 라이브러리로, 모든 모듈에서 사용할 수 있는 가벼운 클래스들을 포함합니다.
*   **Moved from `libs/core`**:
    *   `exception/*` (`BadRequestException`, `NotFoundException`, etc.)
    *   `dto/*` (`BaseRes`, `ErrorRes`)
    *   **NOT** including `BaseTimeEntity` (JPA dependency) or Configs.
*   **Dependencies**:
    *   `jackson-annotations`
    *   `spring-boot-starter-validation` (선택적, 필요한 경우만)
    *   `swagger-annotations` (DTO 문서화용)

### 2. New Library: `libs/storage-redis` (Redis Infrastructure)
Redis 관련 설정 및 유틸리티를 모아둔 라이브러리입니다.
*   **Moved from `libs/core`**:
    *   `config/Redis*` (`RedisProps`, etc.)
    *   `config/TimerProps` -> **Refactor to `TimerRedisProps`**
        *   `Expiration` 및 `PubSub` 설정만 포함합니다.
        *   `ownerTokenHeader`는 제외합니다.
*   **Dependencies**:
    *   `spring-boot-starter-data-redis`
    *   `common` (if needed)

### 3. New Library: `libs/db-jpa` (JPA Infrastructure)
JPA 관련 공통 기능을 모아둔 라이브러리입니다.
*   **Moved from `libs/core`**:
    *   `domain/BaseTimeEntity`
*   **Dependencies**:
    *   `spring-boot-starter-data-jpa`

### 4. Refactor: `libs/web-support` (Web Support)
웹 애플리케이션(API Server 등)을 위한 공통 설정을 담당합니다.
*   **Moved from `libs/core`**:
    *   `config/WebClientProps`
    *   `config/TimerProps` -> **Extract to `TimerWebProps`** (or similar)
        *   `ownerTokenHeader` 설정만 포함합니다.
*   **Retain**:
    *   `GlobalExceptionHandler`
*   **Dependencies**:
    *   `common` (Exception, DTO 참조)
    *   `spring-web`

### 5. Cleanup: `libs/core`
*   위의 모듈로 분리 후 `libs/core`는 **삭제**하거나, 레거시 호환성을 위해 `libs/common`을 가리키도록 변경합니다. (이번 계획에서는 **삭제 및 대체**를 권장)

---

### Directory Structure Changes

```text
libs/
  ├── common/              <-- [NEW] Pure Shared (Exceptions, DTOs)
  ├── storage-redis/       <-- [NEW] Redis Configs
  ├── db-jpa/              <-- [NEW] JPA Base Entity
  ├── web-support/         <-- [UPDATED] Web Utils, GlobalHandler
  └── core/                <-- [DELETE] Dispersed to above
```

### Package Naming Strategy
폴더명에는 하이픈(-)을 사용하지만, Java Package Name에는 하이픈을 사용할 수 없으므로 아래 규칙을 따릅니다.
*   `libs/common` -> `package com.sharetimer.common;`
*   `libs/storage-redis` -> `package com.sharetimer.storage.redis;`
*   `libs/db-jpa` -> `package com.sharetimer.storage.jpa;`
*   `libs/web-support` -> `package com.sharetimer.web.support;` (기존 `com.sharetimer.web`에서 변경 고려)

## Verification Plan

### Automated Tests
1.  **Build Verification**: `./gradlew clean build`를 실행하여 모든 모듈(`apps/*` 포함)이 정상적으로 빌드되는지 확인합니다.
2.  **App Startup**: `api-gateway`, `sync-service` 등 주요 앱을 실행하여 Context Load가 성공하는지 확인합니다.

### Manual Verification
1.  앱 구동 로그 확인.
2.  Redis 연결 및 JPA Entity 인식 확인 (기존 기능 동작 점검).
