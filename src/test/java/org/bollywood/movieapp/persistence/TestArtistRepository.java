package org.bollywood.movieapp.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.bollywood.movieapp.entity.Artist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;



@DataJpaTest
class TestArtistRepository{

	
	
	@Autowired
	private ArtistRepository artistRepository;

	@Autowired
	private EntityManager entityManager;

	@ParameterizedTest
	@ValueSource(strings = { "Z", "alfred hitchcock"})
		void testFindbyName(String name) {
		// given
		// 1 - a name of artists to read in the test
		//String name = "john cuzack";
		// 2 - writing data in database via the entity manager
		List<Artist> artistsDatabase = List.of(
				new Artist("steve mc queen", LocalDate.of(1930, 3, 24), LocalDate.of(1980, 11, 7)),
				new Artist("alfred hitchcock", LocalDate.of(1930, 3, 24)),
				new Artist("john cuzack", LocalDate.of(1930, 3, 24)),
				new Artist("dwayne johnson"));
		
		
		// je demande pour tous les artists d'appliquer la méthode persist de la class entitymanager
		artistsDatabase.forEach(entityManager::persist); // SQL: insert for each artist
		
		
		// on s'assure que tous les inserts ont été faits => on fait un flush
		entityManager.flush();

		// when: read from the repository
		var artistsFound = artistRepository.findByName(name);
//				findAll();
		// then
		
		/**
		 * commentaires à ne pas laisser en prod
		 */
//		System.out.println(artistssDatabase);
//		System.out.println(artistsFound);
		
		
		assertEquals(1,artistsFound.size());
		
		assertAll(artistsFound.stream().map(m-> () -> assertEquals(m.getName(),m.getName(),"name")));
		// est-ce que les films trouvés ont le bon titre

		
	}

}
