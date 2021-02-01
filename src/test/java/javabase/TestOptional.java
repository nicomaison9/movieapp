package javabase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class TestOptional {

	

	@Test
	void testEmptyOptional() {
		Optional<String> optString=Optional.empty();
		System.out.println(optString);
		assertTrue(optString.isEmpty());
		optString.ifPresent(System.out::println);
		
		//génère une erreur
		//optString.get();   
	}
	
	@Test
	void testNotEmptyOptional() {
		Optional<String> optString=Optional.of("Blade Runner");
		System.out.println(optString);
		assertTrue(optString.isPresent());
		optString.ifPresent(System.out::println);
		Optional<String> res=optString.map(String::toUpperCase);
		//map applique une fonction (ici uppercase) uniquement si la donnée est là (ici en String)
		System.out.println(res);
	}

}
