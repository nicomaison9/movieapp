package org.bollywood.movieapp.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.bollywood.movieapp.entity.Artist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TestArtistRepositoryFind {

	@Autowired
	private ArtistRepository artistRepository;

	@Autowired
	private EntityManager entityManager;

	List<Artist> artists;
	List<Integer> ids;

	@BeforeEach
	void initdata() {
		// write data in database(via hibernate entity manager
		artists = List.of(new Artist("steve mc queen", LocalDate.of(1930, 3, 24), LocalDate.of(1980, 11, 7)),
				new Artist("alfred hitchcock", LocalDate.of(1930, 3, 24)),
				new Artist("john cuzack", LocalDate.of(1930, 3, 24)),
				new Artist("steve mc queen", LocalDate.of(1930, 3, 24)),
				new Artist("steven R. mc queen", LocalDate.of(1988, 07, 13)), new Artist("dwayne johnson"));
		artists.forEach(entityManager::persist);
		entityManager.flush();
		ids = artists.stream()
				// .map(a->a.getId())
				.map(Artist::getId).collect(Collectors.toList());
	}

//	@ParameterizedTest
//	@ValueSource(strings = { "Z", "alfred hitchcock"})
//		void testFindbyName(String name) {
//		
//
//		var artistsFound = artistRepository.findByName(name);
//
//	
//		// est-ce que les films trouvés ont le bon titre
////		assertEquals(1,artistsFound.size());
////		
////		assertAll(artistsFound.stream().map(m-> () -> assertEquals(m.getName(),m.getName(),"name")));
//		
//	}

	@Test
	void testFindAll() {
		var artistsFound1 = artistRepository.findAll();
		System.out.println(artistsFound1);
		System.out.println(ids);
		// TODO asserts
	}

	@Test
	void testFindbyId() {
		var id = ids.get(0);
		var artistsFound2 = artistRepository.findById(id);
		System.out.println("artistsFound in DB" + artistsFound2);
		System.out.println("ids asked         " + id);
		System.out.println(artistsFound2);
		System.out.println(id);

		// TODO asserts
		assertTrue(artistsFound2.isPresent(), "artist Found");
		artistsFound2.ifPresent(a -> assertEquals(id, a.getId(), "id artist"));

	}

	@Test
	void testFindAllbyId() {
		var idselection = List.of(ids.get(0), ids.get(0));
		var artistsFound2 = artistRepository.findAllById(idselection);
		System.out.println("artistsFound in DB" + artistsFound2);
		System.out.println("ids asked         " + ids);
		System.out.println(artistsFound2);
		System.out.println(idselection);

		// TODO asserts
//		assertTrue(artistsFound2.isPresent(),"artist Found");
//		artistsFound2.ifPresent(a->assertEquals(id,a.getId(),"id artist"));

	}

	@Test
	void testGetOne() {
		var id = ids.get(0);
		var artistsFound1 = artistRepository.getOne(id);
		System.out.println(artistsFound1);
		System.out.println(ids);

		// TODO asserts

	}

	@Test
	void testfindByNameIgnoreCase() {
		String name = "steve mc queen";
		var artists = artistRepository.findByNameIgnoreCase(name);
		System.out.println(artists);
		for (var a : artists) {
			assertEquals(name, a.getName());
		}
		assertAll(artists.stream().map(a -> () -> assertEquals(name, a.getName())));
	}

	@Test
	void testfindByNameEndingWithIgnoreCase() {
		String name = "mc queen";
		var artists = artistRepository.findByNameEndingWithIgnoreCase(name);
		System.out.println(artists);
		assertAll(artists
				//.map(a->{System.out.println(a);return a;})
				.map(a-> ()-> assertTrue(a.getName().toLowerCase().endsWith(name))));
	}
}
