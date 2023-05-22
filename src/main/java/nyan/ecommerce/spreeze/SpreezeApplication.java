package nyan.ecommerce.spreeze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpreezeApplication {

	public static void main(String[] args) {
		System.out.println("${spring.data.mongodb.uri}");
		SpringApplication.run(SpreezeApplication.class, args);
	}

}
