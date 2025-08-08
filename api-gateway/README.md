# API Gateway

Spring Cloud Gateway 기반의 API Gateway 서비스입니다.

## 🏗️ 아키텍처 개요

API Gateway는 마이크로서비스 아키텍처에서 모든 클라이언트 요청의 진입점 역할을 합니다.

### 주요 기능
- **라우팅**: 요청을 적절한 마이크로서비스로 전달
- **인증/인가**: JWT 토큰 기반 사용자 인증
- **로드 밸런싱**: 서비스 인스턴스 간 요청 분산
- **모니터링**: 요청/응답 로깅 및 메트릭 수집
- **보안**: CORS, Rate Limiting 등 보안 정책 적용

---

## 💻 개발 환경

- **Java**: 17 (Amazon Corretto)
- **Spring Boot**: 3.5.4
- **Spring Cloud**: 2025.0.0
- **Gradle**: 8.x
- **JWT**: HS256 (HMAC SHA-256) + Base64 인코딩
- **MSA 구조**: Spring Cloud Gateway

---

## 📦 프로젝트 초기 셋업

### 1. 프로젝트 클론 및 빌드
```bash
# 프로젝트 클론
git clone <repository-url>
cd Backend-ApiGateway/api-gateway

# 의존성 설치 및 빌드
./gradlew clean build
```

### 2. 환경 변수 설정
```bash
# .env 파일 생성
cp env.example .env

# .env 파일 편집 (실제 값으로 변경)
nano .env
```

### 3. IDE 설정

#### **IntelliJ IDEA:**
1. **EnvFile** 플러그인 설치 (File → Settings → Plugins)
2. Run Configuration에서 **EnvFile** 탭 선택
3. **Enable EnvFile** 체크 후 `.env` 파일 경로 설정

#### **VS Code:**
1. **DotENV** 확장 설치
2. `.vscode/launch.json` 파일이 자동으로 생성됨
3. F5 키로 디버깅 실행 시 `.env` 파일 자동 로드

#### **Spring Boot 자동 로드:**
- `spring-dotenv` 의존성이 추가되어 애플리케이션 실행 시 자동으로 `.env` 파일 로드

---

## �� 주요 기능 / 구현 목록

### 🔐 JWT 인증 시스템
- **HS256 (HMAC SHA-256)** 알고리즘 사용
- **Base64 인코딩**된 시크릿 키 관리
- 토큰 검증, 사용자 정보 추출, 헤더 추가

### 🛣️ 라우팅 시스템
- **사용자 서비스**: `/user/**`, `/auth/**`
- **알림 서비스**: `/noti/**`
- **대시보드 서비스**: `/home/**`
- **센서 서비스**: `/sensors/**`
- **리포트 서비스**: `/reports/**`
- **챗봇 서비스**: `/chatbot/**`

### 📊 모니터링 및 로깅
- **Actuator**: 헬스 체크, 메트릭, 환경 정보
- **로깅**: 환경별 로그 레벨 설정
- **메트릭**: Micrometer 기반 모니터링

---

## 🛠️ Backend 프로젝트 개발 가이드

### 📁 폴더 구조 및 설정 규칙

```
api-gateway/
├── src/main/java/com/deefacto/api_gateway/
│   ├── config/          # 설정 클래스
│   ├── filter/          # Gateway 필터
│   │   └── JwtAuthFilter.java
│   ├── util/            # 유틸리티 클래스
│   │   └── JwtProvider.java
│   └── ApiGatewayApplication.java
├── src/main/resources/
│   ├── application.yml          # 기본 설정
│   ├── application-dev.yml      # 개발 환경 설정
│   └── application-prod.yml     # 운영 환경 설정
└── .env                         # 환경 변수 (gitignore)
```

### ⚙️ 환경 변수 및 민감 정보 관리

#### **필수 환경 변수**
```bash
# JWT Secret Keys (Base64 인코딩)
JWT_SECRET_KEY_DEV=eW91ci1kZXYtc2VjcmV0LWtleS1oZXJlLW1pbmltdW0tMzItY2hhcmFjdGVycy1sb25n
JWT_SECRET_KEY_PROD=eW91ci1wcm9kdWN0aW9uLXNlY3JldC1rZXktaGVyZS1taW5pbXVtLTMyLWNoYXJhY3RlcnMtbG9uZw==

# 서비스 URL (운영 환경)
USER_SERVICE_URL=http://user-service:8081
NOTIFICATION_SERVICE_URL=http://notification-service:8082
DASHBOARD_SERVICE_URL=http://dashboard-service:8083
SENSORS_SERVICE_URL=http://sensors-service:8084
REPORT_SERVICE_URL=http://report-service:8085
CHATBOT_SERVICE_URL=http://chatbot-service:8086
```

#### **Base64 인코딩 방법**
```bash
# 문자열을 Base64로 인코딩
echo -n "your-secret-key-here-minimum-32-characters-long" | base64

# Base64 디코딩 (확인용)
echo "eW91ci1zZWNyZXQta2V5LWhlcmUtbWluaW11bS0zMi1jaGFyYWN0ZXJzLWxvbmc=" | base64 -d
```

---

## 🚀 실행 방법

### 1. 개발 환경 실행
```bash
# 환경 변수 설정 (Base64 인코딩된 시크릿 키)
export JWT_SECRET_KEY_DEV="base64dummysecretkeyfordocumentationpurposes"

# 애플리케이션 실행
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### 2. 운영 환경 실행
```bash
# 환경 변수 설정 (필수, Base64 인코딩된 시크릿 키)
export JWT_SECRET_KEY_PROD="base64dummysecretkeyfordocumentationpurposes"
export USER_SERVICE_URL="http://user-service:8081"
export NOTIFICATION_SERVICE_URL="http://notification-service:8082"
# ... 기타 서비스 URL

# 애플리케이션 실행
./gradlew bootRun --args='--spring.profiles.active=prod'
```

### 3. Docker 실행
```bash
# Docker 이미지 빌드
docker build -t api-gateway .

# Docker 컨테이너 실행
docker run -p 8080:8080 \
  -e JWT_SECRET_KEY_DEV="your-base64-encoded-secret" \
  -e SPRING_PROFILES_ACTIVE=dev \
  api-gateway
```

---

## 🔐 JWT 인증

### 인증 제외 경로
- `/auth/login` - 로그인
- `/auth/register` - 회원가입

### 인증 헤더
모든 인증된 요청에는 다음 헤더가 추가됩니다:
- `X-Employee-Id`: 사용자 ID
- `X-Role`: 사용자 역할

### JWT 토큰 형식
```bash
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 🌐 주요 URL

| 유형 | URL | 설명 |
|------|-----|------|
| API Gateway | http://localhost:8080 | 메인 API Gateway |
| Actuator | http://localhost:8080/actuator | 모니터링 엔드포인트 |
| Health Check | http://localhost:8080/actuator/health | 헬스 체크 |
| Metrics | http://localhost:8080/actuator/metrics | 메트릭 정보 |

---

## 🧑‍💻 커밋 메시지 컨벤션 (`|` 구분자 사용)

```bash
[type] | sprint | JIRA-KEY | 기능 요약 | 담당자
```

- **type**: feat, fix, docs, config, refactor, test, chore, style 등
- **sprint**: sprint0, sprint1, ...
- **JIRA-KEY**: JIRA 이슈 번호 또는 없음
- **기능 요약**: 핵심 변경 내용
- **담당자**: 실명 또는 닉네임

### 📌 예시
```bash
feat    | sprint0 | 없음     | JWT 인증 필터 구현         | KIM
feat    | sprint0 | IOT-123  | API Gateway 라우팅 설정    | KIM
fix     | sprint1 | IOT-210  | JWT 토큰 검증 오류 수정    | RAFA
config  | sprint0 | IOT-001  | Base64 인코딩 적용         | MO
docs    | sprint1 | IOT-999  | API Gateway README 작성    | JONE
```

### ✅ 추천 커밋 예시 (복붙용)
```bash
git commit -m "feat    | sprint1 | IOT-112 | JWT 인증 필터 구현         | KIM"
git commit -m "fix     | sprint0 | IOT-009 | Base64 디코딩 오류 수정    | RAFA"
git commit -m "config  | sprint0 | IOT-000 | Spring Cloud 2025.0.0 적용 | MO"
git commit -m "chore   | sprint1 | IOT-999 | API Gateway README 정리    | JONE"
```

---

## 🔒 보안 / 주의사항

### 민감 정보 관리
- `.env`, `src/main/resources/certs/` 등 민감 파일은 **git에 커밋 금지**
- `.gitignore`에 이미 포함되어 있음
- 환경 변수/비밀키는 운영 서버 또는 CI/CD에서 안전하게 주입

### JWT 보안
- **HS256 (HMAC SHA-256)** 알고리즘 사용
- **Base64 인코딩**된 시크릿 키 필수
- 최소 32자 이상의 원본 문자열을 Base64로 인코딩
- 환경별로 다른 시크릿 키 사용

### 환경별 설정 차이점

| 설정 | 개발 환경 | 운영 환경 |
|------|-----------|-----------|
| 로깅 레벨 | DEBUG | INFO/WARN |
| Actuator 엔드포인트 | 모든 엔드포인트 | 제한된 엔드포인트 |
| 서비스 URL | localhost | 환경 변수 |
| JWT 시크릿 | 기본값 허용 | 환경 변수 필수 |

---

## 🧪 테스트

### 단위 테스트
```bash
# 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests JwtProviderTest
```

### 통합 테스트
```bash
# 통합 테스트 실행
./gradlew integrationTest
```

### API 테스트
```bash
# JWT 토큰 생성 테스트
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test"}'

# 인증된 요청 테스트
curl -X GET http://localhost:8080/user/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 🚧 기타 운영 참고

### 로그 확인
```bash
# 애플리케이션 로그 확인
tail -f logs/api-gateway-dev.log

# Docker 로그 확인
docker logs -f api-gateway-container
```

### 메트릭 모니터링
```bash
# JVM 메트릭 확인
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# HTTP 요청 메트릭 확인
curl http://localhost:8080/actuator/metrics/http.server.requests
```

### 헬스 체크
```bash
# 헬스 체크 확인
curl http://localhost:8080/actuator/health

# 상세 헬스 정보 확인
curl http://localhost:8080/actuator/health -H "Accept: application/json"
```

---

## 📚 참고 자료

- [Spring Cloud Gateway 공식 문서](https://spring.io/projects/spring-cloud-gateway)
- [JWT.io](https://jwt.io/) - JWT 토큰 디버거
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Base64 인코딩/디코딩](https://www.base64encode.org/)

---

## 🤝 기여 가이드

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat | sprint1 | IOT-123 | Add amazing feature | YOUR_NAME'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📞 문의

- **프로젝트 관리자**: [KIM]
- **기술 문의**: [RAFA]
- **이슈 리포트**: [GitHub Issues 링크] 