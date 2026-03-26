package com.ndiamond.paintshop;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;
import java.util.Base64;

@SpringBootApplication
public class PaintShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaintShopApplication.class, args);

//		// Generate secure 256-bit key for HS256
//		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//		String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
//		System.out.println("Base64 key: " + base64Key);
	}

}
