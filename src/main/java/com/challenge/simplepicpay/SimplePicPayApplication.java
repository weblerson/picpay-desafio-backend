package com.challenge.simplepicpay;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "SimplePicPay API", version = "1.0", description = "SimplePicPay API"))
public class SimplePicPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplePicPayApplication.class, args);
	}

}
