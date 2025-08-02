package com.deefacto.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway 메인 애플리케이션 클래스
 * 
 * Spring Cloud Gateway를 사용하여 마이크로서비스 아키텍처의 
 * 모든 API 요청의 진입점 역할을 수행하는 애플리케이션
 * 
 * 주요 기능:
 * - 모든 마이크로서비스로의 라우팅
 * - JWT 기반 인증 및 권한 검증
 * - 요청/응답 로깅 및 모니터링
 * - 서비스 디스커버리 및 로드 밸런싱
 * 
 * @author API Gateway Team
 * @version 1.0
 */
@SpringBootApplication
public class ApiGatewayApplication {

	/**
	 * 애플리케이션 시작점
	 * 
	 * Spring Boot 애플리케이션을 시작하고 
	 * Spring Cloud Gateway 설정을 활성화
	 * 
	 * @param args 명령행 인수
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
