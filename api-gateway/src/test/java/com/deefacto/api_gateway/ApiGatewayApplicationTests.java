package com.deefacto.api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.cloud.gateway.routes[0].id=admin-service",
    "spring.cloud.gateway.routes[0].uri=http://localhost:8081",
    "spring.cloud.gateway.routes[0].predicates[0]=Path=/admin/**",
    "spring.cloud.gateway.routes[1].id=user-service",
    "spring.cloud.gateway.routes[1].uri=http://localhost:8082",
    "spring.cloud.gateway.routes[1].predicates[0]=Path=/user/**",
    "spring.cloud.gateway.routes[2].id=notification-service",
    "spring.cloud.gateway.routes[2].uri=http://localhost:8083",
    "spring.cloud.gateway.routes[2].predicates[0]=Path=/noti/**",
    "spring.cloud.gateway.routes[3].id=dashboard-service",
    "spring.cloud.gateway.routes[3].uri=http://localhost:8084",
    "spring.cloud.gateway.routes[3].predicates[0]=Path=/home/**",
    "spring.cloud.gateway.routes[4].id=sensors-service",
    "spring.cloud.gateway.routes[4].uri=http://localhost:8085",
    "spring.cloud.gateway.routes[4].predicates[0]=Path=/sensors/**",
    "spring.cloud.gateway.routes[5].id=report-service",
    "spring.cloud.gateway.routes[5].uri=http://localhost:8086",
    "spring.cloud.gateway.routes[5].predicates[0]=Path=/reports/**",
    "spring.cloud.gateway.routes[6].id=chatbot-service",
    "spring.cloud.gateway.routes[6].uri=http://localhost:8087",
    "spring.cloud.gateway.routes[6].predicates[0]=Path=/chatbot/**"
})
class ApiGatewayApplicationTests {

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
        // 기본 컨텍스트 로드 테스트
    }

    @Test
    void testGatewayHealthEndpoint() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        client.get().uri("/actuator/health")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGatewayInfoEndpoint() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        client.get().uri("/actuator/info")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGatewayMetricsEndpoint() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        client.get().uri("/actuator/metrics")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGatewayRoutesEndpoint() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        client.get().uri("/actuator/gateway/routes")
                .exchange()
                .expectStatus().isOk();
    }
}
