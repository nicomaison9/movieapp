package javabase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.internal.build.AllowSysOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class testStreams {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testMapForEach() {
		List<String> cities = List.of("toulouse","pau","nantes","nîmes","saint petersbourg");
		cities.forEach(c->System.out.println(c));
		cities.stream().map(c -> c.toUpperCase()).forEach(c->System.out.println(c));
		// équivalent; :: équivaut à dire on prend méthode rangée dans une classe
		cities.stream().map(String::toUpperCase).forEach(c->System.out.println(c));
		cities.forEach(c->System.out.println(c));
		//équivalent à
		cities.forEach(System.out::println);
		System.out.println("---------------------------------------");
		
	}
	@Test
	void testMapFilterCollect() {
		List<String> cities = List.of(" tOULOuse"," pAU"," NANTes","nîMES "," saint PETERsbourg ");
		var res= cities.parallelStream()  // intéressant pour des grandes listes sinon utiliser .Stream()
		.map(String::trim).map(c-> c.toLowerCase()) // trim = enlève espaces avant et après
		.filter(c->c.startsWith("n"))    //String > boolean
		.collect(Collectors.toList());// récupère une donnée en mémoire
		
		
		//collecte nécessite:
		// -> 1 accumulateur
		// -> 1 opération pour chaque donnée du stream
		// -> 1 opération de finalisation (accumulateur -> ?)
		// -> 1 opération de combinaison(travail en parallèle)
//		.forEach(c->System.out.println(c));
		
		System.out.println(res);
		System.out.println("---------------------------------------");
}
	@Test
	void testMapFilterCollectToStats() {
		List<String> cities = List.of(" tOULOuse"," pAU"," NANTes","nîMES "," saint PETERsbourg ");
		var res= cities.parallelStream()  // intéressant pour des grandes listes sinon utiliser .Stream()
		.map(String::trim)
		.map(c-> c.toLowerCase()) // trim = enlève espaces avant et après
		.filter(c->c.startsWith("n"))
		.mapToInt(String::length)
//		.sum();
//		.average();
//		.count();
//		.min();
//		.reduce();
		.summaryStatistics();
//		.reduce(null);
		
		
		
		System.out.println(res);
		System.out.println("---------------------------------------");
}
}
