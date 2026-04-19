# booking-platform
다양한 예약 서비스를 통합한 플랫폼입니다.
사용자는 스포츠 시설, 숙박, 음식점 등을 검색하고 예약할 수 있으며,
서비스 제공자는 자신의 서비스를 등록하고 관리할 수 있습니다.

이 프로젝트는 분산 환경에서의 서비스 설계와
REST API 및 GraphQL을 동시에 활용하는 구조를 학습하기 위해 개발되었습니다.

## 🛠 Tech Stack
- Java (LTS-Long-Term Support)
- Spring Boot 3 + Spring Security (Session) + Hibernate
- PostgreSQL
- Hybrid API architecture: REST (Auth, Upload) + GraphQL (Feed, Query) + WebSocket (Notify)

## 📌 Main Features
### 👤 User-사용자
- 서비스 검색 및 필터링
- 예약 생성 및 취소
- 리뷰 작성 및 조회

### 🧑‍💼 Provider-제공자
- 서비스 등록 및 수정
- 예약 상태 관리

### 🛠 Admin-관리
- 사용자 및 서비스 관리
- 시스템 모니터링

## 🛢 Database Design
- User
- Service
- Booking
- Review
- Notification

## ➣ Architecture
Client → API Gateway → Service Layer → Database

## 🧩 Project Structure
```
backend
 ├── config            # Application configuration
 ├── controller        # REST Controller
 ├── entity            # JPA entities
 ├── dto               # Data Transfer Objects
 ├── exception         # @RestControllerAdvice
 ├── log               # activity app logging
 ├── interceptor       # Rate limiting, request interception
 ├── mapper            # Mapping entity <-> DTO
 ├── projection        # GraphQL dto projection
 ├── repository        # Spring Data JPA
 ├── resolver          # Queries, Mutation, @SchemaMapping / @BatchMapping
 │   └── batchloader 
 ├── service           # Business logic
 │   └── auth          # signup, login, refresh token
 ├── security          # JWT / OAuth2
 ├── status            # Enum definitions
 └── ws                # WebSocket config

frontend
 └── graphql           # GraphQL queries & mutations

```
