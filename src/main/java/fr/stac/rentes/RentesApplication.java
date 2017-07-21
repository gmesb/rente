package fr.stac.rentes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableAutoConfiguration
public class RentesApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder rentesApplication) {
		return rentesApplication.sources(RentesApplication.class);
	}

	public static void main(String[] args) throws  Exception {
		SpringApplication.run(RentesApplication.class, args);
	}
}
