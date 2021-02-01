package org.bollywood.movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.bollywood.movieapp.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class TestMovieRepository {
//test de la classe movie pour être utilisée dans le repository
	@BeforeEach
	void setUp() throws Exception {
	}

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void testFindbyTitle() {
		// given
		// 1 - a title of movies to read in the test
		String title = "The man who knew too much";
		// 2 - writing data in database via the entity manager
		List<Movie> moviesDatabase = List.of(
				new Movie(title, 1934, null),
				new Movie(title, 1943, null), 
				new Movie(title, 1952, null),
				new Movie("the man who knew too little", 1999, null));
		
		
		// je demande pour tous les movies d'appliquer la méthode persist de la class entitymanager
		moviesDatabase.forEach(entityManager::persist); // SQL: insert for each movie
		
		
		// on s'assure que tous les inserts ont été faits => on fait un flush
		entityManager.flush();

		// when: read from the repository
		var moviesFound = movieRepository.findByTitle(title);
		// then
		
		/**
		 * commentaires à ne pas laisser en prod
		 */
//		System.out.println(moviesDatabase);
//		System.out.println(moviesFound);
		
		
		assertEquals(3,moviesFound.size());
		
		// est-ce que les films trouvés ont le bon titre
		final var title2="Dummy";
		
		assertAll(moviesFound.stream().map(m-> () -> assertEquals(title2,m.getTitle(),"title")));
		for(Movie m:moviesFound)
		{
			assertEquals(title,m.getTitle(),"title");
			}
		
	}		

	@ParameterizedTest
//	@ValueSource(ints= {1888,1982,Integer.MAX_VALUE})
	@ValueSource(strings = { "Z", "Blade Runner",
			"Night of the day of the down of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the" })

	void testSaveTitle(String title) {
		// given (hypothèses de départ
//		String  title="blade runner";
		int year = 1982;
		int duration = 175;
		saveAssertMovie(title, year, duration);
	}

	@Test
	void testSaveTitleEmptyNOK() {
		// given (hypothèses de départ
		String title = null;
		int year = 1982;
		int duration = 175;
		assertThrows(DataIntegrityViolationException.class, () -> saveAssertMovie(title, year, duration));
	}

	@ParameterizedTest
	@ValueSource(ints = { 1888, 1982, Integer.MAX_VALUE })
	void testSaveYear(int year) {
		// given (hypothèses de départ
		String title = "blade runner";
//		int year = 1982;
		int duration = 175;
		saveAssertMovie(title, year, duration);
	}

	@Test
	void testSaveYearNullNOK() {
		// given
		String title = "blade runner";
		Integer year = null;
		int duration = 175;
		assertThrows(DataIntegrityViolationException.class, () -> saveAssertMovie(title, year, duration));
		// assertthrows pour tests négatifs
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 150, Integer.MAX_VALUE })
	@NullSource // nullsource pour tests positifs
	void testSaveDuration(Integer duration) {
		// given
		String title = "blade runner";
		int year = 1982;

		saveAssertMovie(title, year, duration);
	}

	private void saveAssertMovie(String title, Integer year, Integer duration) {
		Movie movie = new Movie(title, year, duration);
		// when
		movieRepository.save(movie);

		// then
		var idMovie = movie.getId();
		assertNotNull(idMovie);
//		var optmovieFromRepo = movieRepository.findById(idMovie);
//		var movieFromRepo = optmovieFromRepo.get();
//		assertEquals(movie,movieFromRepo);
		movieRepository.findById(idMovie).ifPresent(m -> assertEquals(movie, m));
//		 équivalent à:
//		 movieRepository.findById(idMovie).ifPresent((Movie m) -> assertEquals(movie,m));

//		 debug purpose only
//		System.out.println(movie);
	}

}
