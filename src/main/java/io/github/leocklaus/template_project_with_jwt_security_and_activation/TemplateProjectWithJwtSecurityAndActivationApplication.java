package io.github.leocklaus.template_project_with_jwt_security_and_activation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TemplateProjectWithJwtSecurityAndActivationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemplateProjectWithJwtSecurityAndActivationApplication.class, args);
	}

}
