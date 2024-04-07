package com.rossypotentials.bankApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
			title = "Banking Application",
			description = "Backend REST APIs for Banking Application",
			version = "v.1.0",
			contact = @Contact(
					name = "Uroko Rosemary Nnena",
					email = "urokorosemarynnena@gmail.com",
					url = "https://www.linkedin.com/in/rosemary-nnena-uroko-654584219/"
			),
				license =@License(
						name = "Bank Application",
						url = "https://github.com/Rossypotentials1"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Backend REST APIs for Banking Application",
				url = "https://github.com/Rossypotentials1"
		)
)
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}
