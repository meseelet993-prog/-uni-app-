package com.mentalhealth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mentalhealth.mapper")
public class MentalHealthPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(MentalHealthPlatformApplication.class, args);
	}
}