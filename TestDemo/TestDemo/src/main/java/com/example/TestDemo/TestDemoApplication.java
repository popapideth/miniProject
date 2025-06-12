package com.example.TestDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableAsync
@SpringBootApplication
public class TestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestDemoApplication.class, args);
	}

//	บอก Spring Boot ว่า http://localhost:4200 (Angular app) ได้รับอนุญาต ให้เรียก API
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:4200")
						.allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS")
						.allowCredentials(false)
						.allowedHeaders("*");
			}
		};
	}

//	@GetMapping
//	public List<String> hello(){
//		return List.of("Hello world");
//	}
}
