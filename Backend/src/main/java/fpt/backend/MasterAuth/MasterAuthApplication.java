package fpt.backend.MasterAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MasterAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(MasterAuthApplication.class, args);
	}

}
