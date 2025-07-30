# API Gateway - Smart Factory Cleanroom Environment Monitoring Dashboard

## 개요
이 API Gateway는 스마트팩토리 클린룸 환경 모니터링 대시보드의 마이크로서비스 아키텍처에서 모든 요청의 진입점 역할을 합니다.

## 기술 스택
- **Spring Boot 3.5.4**
- **Spring Cloud Gateway 2025.0.0**
- **Java 17**
- **Gradle**

## 서비스 구성
API Gateway는 다음 마이크로서비스들과 연동됩니다:

| 서비스 | 포트 | 경로 | 설명 |
|--------|------|------|------|
| Auth Service | 8081 | `/auth/**` | 인증 및 권한 관리 |
| User Service | 8082 | `/user/**` | 사용자 관리 |
| Sensor Service | 8083 | `/sensor/**` | 센서 데이터 관리 |
| Notification Service | 8084 | `/notification/**` | 알림 관리 |

## 특화 엔드포인트
- **대시보드 API**: `/dashboard/**` → Sensor Service (데이터 집계)
- **IoT 데이터 수집**: `/iot/data/**` → Sensor Service (센서 데이터 수집)
- **실시간 알림**: `/ws/notifications/**` → Notification Service (WebSocket)
- **관리자 기능**: `/admin/**` → User Service (관리자 전용)

## 실행 방법

### 1. 빌드
```bash
./gradlew build
```

### 2. 실행
```bash
./gradlew bootRun
```

### 3. Docker 실행 (선택사항)
```bash
docker build -t api-gateway .
docker run -p 8080:8080 api-gateway
```

## 모니터링 및 헬스 체크

### Actuator 엔드포인트
- **헬스 체크**: `GET /actuator/health`
- **애플리케이션 정보**: `GET /actuator/info`
- **메트릭**: `GET /actuator/metrics`
- **Gateway 정보**: `GET /actuator/gateway`

### 로깅
- **로그 레벨**: DEBUG (Gateway 관련)
- **로그 패턴**: `yyyy-MM-dd HH:mm:ss - message`

## 테스트

### 단위 테스트 실행
```bash
./gradlew test
```

### 통합 테스트 실행
```bash
./gradlew integrationTest
```

## 설정 파일

### application.yml
주요 설정:
- **서버 포트**: 8080
- **라우팅 규칙**: 각 서비스별 경로 매핑
- **로깅**: Gateway 디버그 로깅 활성화
- **Actuator**: 모니터링 엔드포인트 노출

## 개발 가이드

### 새로운 필터 추가
1. `filter` 패키지에 새로운 필터 클래스 생성
2. `GlobalFilter` 인터페이스 구현
3. `@Component` 어노테이션 추가
4. `getOrder()` 메서드로 실행 순서 설정

### 새로운 라우트 추가
1. `application.yml`에 새로운 라우트 정의
2. Predicate와 Filter 설정
3. 테스트 케이스 작성

## 배포

### 환경별 설정
- **개발**: `application-dev.yml`
- **스테이징**: `application-staging.yml`
- **프로덕션**: `application-prod.yml`

### 환경 변수
- `SERVER_PORT`: 서버 포트 (기본값: 8080)
- `SPRING_PROFILES_ACTIVE`: 활성 프로파일

## 문제 해결

### 일반적인 문제
1. **포트 충돌**: 다른 서비스가 8080 포트를 사용 중인지 확인
2. **서비스 연결 실패**: 각 마이크로서비스가 실행 중인지 확인
3. **라우팅 실패**: application.yml의 라우트 설정 확인

### 로그 확인
```bash
tail -f logs/api-gateway.log
```

## 라이센스
이 프로젝트는 내부 사용을 위한 것입니다. 