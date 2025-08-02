// package com.deefacto.api_gateway;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.DisplayName;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.test.web.reactive.server.WebTestClient;
// import org.springframework.http.MediaType;

// import static org.hamcrest.Matchers.*;

// /**
//  * API Gateway 애플리케이션 통합 테스트
//  * 
//  * 주요 테스트 항목:
//  * - 애플리케이션 컨텍스트 로드
//  * - Actuator 엔드포인트 동작 확인
//  * - 기본 라우팅 설정 확인
//  * - 헬스체크 및 모니터링 기능
//  */
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @TestPropertySource(properties = {
//     "spring.cloud.gateway.routes[0].id=admin-service",
//     "spring.cloud.gateway.routes[0].uri=http://localhost:8081",
//     "spring.cloud.gateway.routes[0].predicates[0]=Path=/admin/**",
//     "spring.cloud.gateway.routes[1].id=user-service",
//     "spring.cloud.gateway.routes[1].uri=http://localhost:8082",
//     "spring.cloud.gateway.routes[1].predicates[0]=Path=/user/**",
//     "spring.cloud.gateway.routes[2].id=notification-service",
//     "spring.cloud.gateway.routes[2].uri=http://localhost:8083",
//     "spring.cloud.gateway.routes[2].predicates[0]=Path=/noti/**",
//     "spring.cloud.gateway.routes[3].id=dashboard-service",
//     "spring.cloud.gateway.routes[3].uri=http://localhost:8084",
//     "spring.cloud.gateway.routes[3].predicates[0]=Path=/home/**",
//     "spring.cloud.gateway.routes[4].id=sensors-service",
//     "spring.cloud.gateway.routes[4].uri=http://localhost:8085",
//     "spring.cloud.gateway.routes[4].predicates[0]=Path=/sensors/**",
//     "spring.cloud.gateway.routes[5].id=report-service",
//     "spring.cloud.gateway.routes[5].uri=http://localhost:8086",
//     "spring.cloud.gateway.routes[5].predicates[0]=Path=/reports/**",
//     "spring.cloud.gateway.routes[6].id=chatbot-service",
//     "spring.cloud.gateway.routes[6].uri=http://localhost:8087",
//     "spring.cloud.gateway.routes[6].predicates[0]=Path=/chatbot/**",
//     // Actuator 설정
//     "management.endpoints.web.exposure.include=health,info,metrics,gateway",
//     "management.endpoint.health.show-details=always"
// })
// class ApiGatewayApplicationTests {

//     @LocalServerPort
//     private int port;

//     private WebTestClient webTestClient;

//     @Test
//     @DisplayName("애플리케이션 컨텍스트가 정상적으로 로드되는지 확인")
//     void contextLoads() {
//         // Spring Boot 애플리케이션이 정상적으로 시작되는지 확인
//         // 테스트가 여기까지 도달하면 컨텍스트 로드 성공
//     }

//     @Test
//     @DisplayName("Gateway 헬스체크 엔드포인트가 정상 동작하는지 확인")
//     void testGatewayHealthEndpoint() {
//         webTestClient = WebTestClient.bindToServer()
//                 .baseUrl("http://localhost:" + port)
//                 .build();

//         webTestClient.get().uri("/actuator/health")
//                 .accept(MediaType.APPLICATION_JSON)
//                 .exchange()
//                 .expectStatus().isOk()
//                 .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                 .expectBody()
//                 .jsonPath("$.status").isEqualTo("UP")
//                 .jsonPath("$.components.gateway.status").isEqualTo("UP");
//     }

//     @Test
//     @DisplayName("Gateway 정보 엔드포인트가 정상 동작하는지 확인")
//     void testGatewayInfoEndpoint() {
//         webTestClient = WebTestClient.bindToServer()
//                 .baseUrl("http://localhost:" + port)
//                 .build();

//         webTestClient.get().uri("/actuator/info")
//                 .accept(MediaType.APPLICATION_JSON)
//                 .exchange()
//                 .expectStatus().isOk()
//                 .expectHeader().contentType(MediaType.APPLICATION_JSON);
//     }

//     @Test
//     @DisplayName("Gateway 메트릭스 엔드포인트가 정상 동작하는지 확인")
//     void testGatewayMetricsEndpoint() {
//         webTestClient = WebTestClient.bindToServer()
//                 .baseUrl("http://localhost:" + port)
//                 .build();

//         webTestClient.get().uri("/actuator/metrics")
//                 .accept(MediaType.APPLICATION_JSON)
//                 .exchange()
//                 .expectStatus().isOk()
//                 .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                 .expectBody()
//                 .jsonPath("$.names").isArray()
//                 .jsonPath("$.names").value(hasItems("jvm.memory.used", "process.cpu.usage"));
//     }

//     @Test
//     @DisplayName("Gateway 라우트 정보 엔드포인트가 정상 동작하는지 확인")
//     void testGatewayRoutesEndpoint() {
//         webTestClient = WebTestClient.bindToServer()
//                 .baseUrl("http://localhost:" + port)
//                 .build();

//         webTestClient.get().uri("/actuator/gateway/routes")
//                 .accept(MediaType.APPLICATION_JSON)
//                 .exchange()
//                 .expectStatus().isOk()
//                 .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                 .expectBody()
//                 .jsonPath("$").isArray()
//                 .jsonPath("$[0].id").isEqualTo("admin-service")
//                 .jsonPath("$[0].predicates[0]").value(containsString("/admin/**"));
//     }

//     @Test
//     @DisplayName("존재하지 않는 엔드포인트에 대한 404 응답 확인")
//     void testNonExistentEndpoint() {
//         webTestClient = WebTestClient.bindToServer()
//                 .baseUrl("http://localhost:" + port)
//                 .build();

//         webTestClient.get().uri("/nonexistent")
//                 .exchange()
//                 .expectStatus().isNotFound();
//     }

//     @Test
//     @DisplayName("Gateway 메모리 사용량 메트릭 확인")
//     void testGatewayMemoryMetrics() {
//         webTestClient = WebTestClient.bindToServer()
//                 .baseUrl("http://localhost:" + port)
//                 .build();

//         webTestClient.get().uri("/actuator/metrics/jvm.memory.used")
//                 .accept(MediaType.APPLICATION_JSON)
//                 .exchange()
//                 .expectStatus().isOk()
//                 .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                 .expectBody()
//                 .jsonPath("$.name").isEqualTo("jvm.memory.used")
//                 .jsonPath("$.measurements").isArray();
//     }

//     @Test
//     @DisplayName("Gateway CPU 사용량 메트릭 확인")
//     void testGatewayCpuMetrics() {
//         webTestClient = WebTestClient.bindToServer()
//                 .baseUrl("http://localhost:" + port)
//                 .build();

//         webTestClient.get().uri("/actuator/metrics/process.cpu.usage")
//                 .accept(MediaType.APPLICATION_JSON)
//                 .exchange()
//                 .expectStatus().isOk()
//                 .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                 .expectBody()
//                 .jsonPath("$.name").isEqualTo("process.cpu.usage")
//                 .jsonPath("$.measurements").isArray();
//     }
// }
