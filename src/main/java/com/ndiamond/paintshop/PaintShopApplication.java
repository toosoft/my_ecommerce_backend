package com.ndiamond.paintshop;

//import io.github.cdimascio.dotenv.Dotenv;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import javax.crypto.SecretKey;
//import java.util.Base64;

@SpringBootApplication
public class PaintShopApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();

		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("DB_URL", dotenv.get("DB_URL"));

		System.setProperty("jwtSecret", dotenv.get("jwtSecret"));
//		System.out.println("URL: " + System.getenv("DB_URL"));

		SpringApplication.run(PaintShopApplication.class, args);


	}

}
