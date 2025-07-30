package com.deefacto.api_gateway.integration;

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
class GatewayRoutingIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    void testAdminServiceRouting() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        // Admin 서비스 라우팅 테스트
        client.get().uri("/admin/users")
                .exchange()
                .expectStatus().is5xxServerError() // 실제 서비스가 없으므로 5xx 에러 예상
                .expectHeader().valueEquals("X-Gateway-Route", "admin-service");
    }

    @Test
    void testUserServiceRouting() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        // User 서비스 라우팅 테스트
        client.get().uri("/user/profile")
                .exchange()
                .expectStatus().is5xxServerError() // 실제 서비스가 없으므로 5xx 에러 예상
                .expectHeader().valueEquals("X-Gateway-Route", "user-service");
    }

    @Test
    void testNotificationServiceRouting() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        // Notification 서비스 라우팅 테스트
        client.get().uri("/noti/alerts")
                .exchange()
                .expectStatus().is5xxServerError() // 실제 서비스가 없으므로 5xx 에러 예상
                .expectHeader().valueEquals("X-Gateway-Route", "notification-service");
    }

    @Test
    void testDashboardServiceRouting() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        // Dashboard 서비스 라우팅 테스트
        client.get().uri("/home/overview")
                .exchange()
                .expectStatus().is5xxServerError() // 실제 서비스가 없으므로 5xx 에러 예상
                .expectHeader().valueEquals("X-Gateway-Route", "dashboard-service");
    }

    @Test
    void testSensorsServiceRouting() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        // Sensors 서비스 라우팅 테스트
        client.get().uri("/sensors/data")
                .exchange()
                .expectStatus().is5xxServerError() // 실제 서비스가 없으므로 5xx 에러 예상
                .expectHeader().valueEquals("X-Gateway-Route", "sensors-service");
    }

    @Test
    void testReportServiceRouting() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        // Report 서비스 라우팅 테스트
        client.get().uri("/reports/monthly")
                .exchange()
                .expectStatus().is5xxServerError() // 실제 서비스가 없으므로 5xx 에러 예상
                .expectHeader().valueEquals("X-Gateway-Route", "report-service");
    }

    @Test
    void testChatbotServiceRouting() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        // Chatbot 서비스 라우팅 테스트
        client.post().uri("/chatbot/message")
                .bodyValue("{\"message\": \"Hello chatbot\"}")
                .exchange()
                .expectStatus().is5xxServerError() // 실제 서비스가 없으므로 5xx 에러 예상
                .expectHeader().valueEquals("X-Gateway-Route", "chatbot-service");
    }

    @Test
    void testNonExistentRoute() {
        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        // 존재하지 않는 라우트 테스트
        client.get().uri("/nonexistent/path")
                .exchange()
                .expectStatus().isNotFound();
    }
} 