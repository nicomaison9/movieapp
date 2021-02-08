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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
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
	void testCount() {
		
		long nb_movies=movieRepository.count(); // fonction d'agregat donnée, par défaut va reprendre dans base de donnée H2, utiliser annotation @AutoConfigureTestDatabase(replace = Replace.NONE)
		System.out.println(nb_movies);
	}

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
		assertAll(moviesFound.stream().map(m-> () -> assertEquals(title,m.getTitle(),"title")));
		// est-ce que les films trouvés ont le bon titre
//		final var title2="Dummy";
//		
//		assertAll(moviesFound.stream().map(m-> () -> assertEquals(title2,m.getTitle(),"title")));
//		for(Movie m:moviesFound)
//		{
//			assertEquals(title,m.getTitle(),"title");
//			}
		
	}	
	
	@Test
	void testfindByYearBetween() {
		int yearmin=1934;
		int yearmax=2010;
		List<Movie> moviesDatabase = List.of(
				new Movie("ze man who knew too much", 1934, 168),
				new Movie("wonderwoman", 1943, 168), 
				new Movie("the man who discussing à l'oreille des chevaux", 1943, 189), 
				new Movie("titanic", 1952, 196),
				new Movie("men in black", 1999, null),
				new Movie("harry potter et l'ordre du phenix", 2000, 210),
				new Movie("spectre", 2009, 123),
				new Movie("island", 2001, 12),
				new Movie("island2", 2010, null));
		moviesDatabase.forEach(entityManager::persist);
		entityManager.flush();
		var moviesByYear = movieRepository.findByYearBetween(yearmin,yearmax,Sort.by("year"));
		System.out.println("--------------movie by year");
		System.out.println(moviesByYear);
		System.out.println("--------------movie by duration desc");
		var moviesByDurationDesc = movieRepository.findByYearBetween(yearmin,yearmax,Sort.by(Direction.DESC,"duration"));
		System.out.println(moviesByDurationDesc);
		System.out.println("--------------movie by duration & title");
		var moviesByDurationTitle = movieRepository.findByYearBetween(yearmin,yearmax,Sort.by("duration","title"));
		System.out.println(moviesByDurationTitle);
	}
	
	@Test
	void testFindbyTitleContainingIgnoreCase() {
		// given
		// 1 - a title of movies to read in the test
		String titlepart = "man";
		// 2 - writing data in database via the entity manager
		List<Movie> moviesDatabase = List.of(
				new Movie("the man who knew too much", 1934, null),
				new Movie("wonderwoman", 1943, null), 
				new Movie("the man who discussing à l'oreille des chevaux", 1943, null), 
				new Movie("titanic", 1952, null),
				new Movie("men in black", 1999, null));
		
		
		// je demande pour tous les movies d'appliquer la méthode persist de la class entitymanager
		moviesDatabase.forEach(entityManager::persist); // SQL: insert for each movie
		
		
		// on s'assure que tous les inserts ont été faits => on fait un flush
		entityManager.flush();

		// when: read from the repository
		var moviesFound = movieRepository.findByTitleContainingIgnoreCase(titlepart);
		// then
		
		/**
		 * commentaires à ne pas laisser en prod
		 */
//		System.out.println(moviesDatabase);
//		System.out.println(moviesFound);
		
		
		assertEquals(3,moviesFound.size());
		assertAll(moviesFound.stream().map(m-> () -> assertTrue(m.getTitle().toLowerCase().contains(titlepart.toLowerCase()),titlepart+" not in title")));
		
		
		
	}	
	@Test
	void testfindByYearGreaterThanEqual() {
		// given
		// 1 - a title of movies to read in the test
		int yearmin = 2000;
		// 2 - writing data in database via the entity manager
		List<Movie> moviesDatabase = List.of(
				new Movie("the man who knew too much", 1934, null),
				new Movie("wonderwoman", 1943, null), 
				new Movie("the man who discussing à l'oreille des chevaux", 1943, null), 
				new Movie("titanic", 2003, null),
				new Movie("men in black", 2007, null));
		
		
		// je demande pour tous les movies d'appliquer la méthode persist de la class entitymanager
		moviesDatabase.forEach(entityManager::persist); // SQL: insert for each movie
		
		
		// on s'assure que tous les inserts ont été faits => on fait un flush
		entityManager.flush();

		// when: read from the repository
		var moviesFound = movieRepository.findByYearGreaterThanEqual(yearmin);
		// then
		
		/**
		 * commentaires à ne pas laisser en prod
		 */
//		System.out.println(moviesDatabase);
//		System.out.println(moviesFound);
		
		
		assertEquals(2,moviesFound.size());
		assertAll(moviesFound.stream().map(m-> () -> assertTrue(m.getYear()>=yearmin,m.getYear() +">="+yearmin)));
		
		
		
	}
	
	@Test
	void testfindByYearBetweenOrderByYear() {
		int yearmin=1934;
		int yearmax=2010;
		List<Movie> moviesDatabase = List.of(
				new Movie("ze man who knew too much", 1934, 168),
				new Movie("wonderwoman", 1943, 168), 
				new Movie("the man who discussing à l'oreille des chevaux", 1943, 189), 
				new Movie("titanic", 1952, 196),
				new Movie("men in black", 1999, null),
				new Movie("harry potter et l'ordre du phenix", 2000, 210),
				new Movie("spectre", 2009, 123),
				new Movie("island", 2001, 12),
				new Movie("island2", 2010, null));
		moviesDatabase.forEach(entityManager::persist);
		entityManager.flush();
		var moviesByYear = movieRepository.findByYearBetweenOrderByYear(yearmin,yearmax,Sort.by("year"));
		System.out.println("--------------movie by year");
		System.out.println(moviesByYear);
		System.out.println("--------------movie by duration");
		var moviesByDurationDesc = movieRepository.findByYearBetweenOrderByYear(yearmin,yearmax,Sort.by(Direction.DESC,"duration"));
		System.out.println(moviesByDurationDesc);
		System.out.println("--------------movie by duration & title");
		var moviesByDurationTitle = movieRepository.findByYearBetweenOrderByYear(yearmin,yearmax,Sort.by("duration","title"));
		System.out.println(moviesByDurationTitle);
	}
//	@Test
//	void testfindByYearBetweenOrderByTitleAsc(int yearmin,int yearmax){
//		// given
//		// 1 - a title of movies to read in the test
////		int yearmin = 2000;
////		int yearmax = 2009;
//		// 2 - writing data in database via the entity manager
//		List<Movie> moviesDatabase = List.of(
//				new Movie("the man who knew too much", 1934, null),
//				new Movie("wonderwoman", 1943, null), 
//				new Movie("the man who discussing à l'oreille des chevaux", 1943, null), 
//				new Movie("titanic", 1952, null),
//				new Movie("men in black", 1999, null),
//				new Movie("harry potter et l'ordre du phenix", 2000, null),
//				new Movie("spectre", 2009, null),
//				new Movie("island", 2001, null),
//				new Movie("island2", 2010, null));
//		
//		
//		// je demande pour tous les movies d'appliquer la méthode persist de la class entitymanager
//		moviesDatabase.forEach(entityManager::persist); // SQL: insert for each movie
//		
//		
//		// on s'assure que tous les inserts ont été faits => on fait un flush
//		entityManager.flush();
//
//		// when: read from the repository
//		var moviesFound = movieRepository.findByYearBetweenOrderByTitleAsc(yearmin,yearmax);
//		// then
//		
//		/**
//		 * commentaires à ne pas laisser en prod
//		 */
////		System.out.println(moviesDatabase);
////		System.out.println(moviesFound);
//		
//		
//		assertEquals(3,moviesFound.size());
//		assertAll(moviesFound.stream().map(
//				m-> () -> assertTrue(
//						m.getYear()>=yearmin && m.getYear()<=yearmax,
//						 "intervalle demandé:" + yearmin+"<="+ m.getYear()+"<="+ yearmax)));
//		
//		
//		
//	}
	
	@Test
	void findByYearOrderByTitle(){
		int year=1980;
		List<Movie> moviesDatabase = List.of(
				new Movie("the man who knew too much", 1934, null),
				new Movie("wonderwoman", 1943, null), 
				new Movie("the man who discussing à l'oreille des chevaux", 1943, null), 
				new Movie("titanic", 1980, null),
				new Movie("The lion king", 1980, null),
				new Movie("The lion king", 1970, null),
				new Movie("men in black", 1980, null));
		
		
		// je demande pour tous les movies d'appliquer la méthode persist de la class entitymanager
		moviesDatabase.forEach(entityManager::persist); // SQL: insert for each movie
		
		
		// on s'assure que tous les inserts ont été faits => on fait un flush
		entityManager.flush();

		// when: read from the repository
		var moviesFound = movieRepository.findByYearOrderByTitle(year);
		System.out.println(moviesFound);
	}
//	@ParameterizedTest
//	
//	void testfindByTitleContainingAndYearEquals(String filmtofind,int yeartofind) {
//		// given
//		// 1 - a title of movies to read in the test
////		String filmtofind="The lion king";
////		int yeartofind = 1980;
//		// 2 - writing data in database via the entity manager
//		List<Movie> moviesDatabase = List.of(
//				new Movie("the man who knew too much", 1934, null),
//				new Movie("wonderwoman", 1943, null), 
//				new Movie("the man who discussing à l'oreille des chevaux", 1943, null), 
//				new Movie("titanic", 1952, null),
//				new Movie("The lion king", 1980, null),
//				new Movie("The lion king", 1969, null),
//				new Movie("men in black", 1999, null));
//		
//		
//		// je demande pour tous les movies d'appliquer la méthode persist de la class entitymanager
//		moviesDatabase.forEach(entityManager::persist); // SQL: insert for each movie
//		
//		
//		// on s'assure que tous les inserts ont été faits => on fait un flush
//		entityManager.flush();
//
//		// when: read from the repository
//		var moviesFound = movieRepository.findByTitleContainingAndYearEquals(filmtofind,yeartofind);
//		// then
//		
//		/**
//		 * commentaires à ne pas laisser en prod
//		 */
////		System.out.println(moviesDatabase);
////		System.out.println(moviesFound);
//		
//		
//		assertEquals(1,moviesFound.size());
//		assertAll(moviesFound.stream().map(m-> () -> assertTrue(m.getTitle().equals(filmtofind) && m.getYear()==yeartofind,filmtofind + " not found")));
//	}
	
	@Test
	void findByDurationNull() {
		// given
		// 1 - a title of movies to read in the test
		
		// 2 - writing data in database via the entity manager
		List<Movie> moviesDatabase = List.of(
				new Movie("the man who knew too much", 1934, 183),
				new Movie("wonderwoman", 1943, null), 
				new Movie("the man who discussing à l'oreille des chevaux", 1943, null), 
				new Movie("titanic", 1952, null),
				new Movie("The lion king", 1980, 175),
				new Movie("The lion king", 1969, null),
				new Movie("men in black", 1999, 200));
		
		
		// je demande pour tous les movies d'appliquer la méthode persist de la class entitymanager
		moviesDatabase.forEach(entityManager::persist); // SQL: insert for each movie
		
		
		// on s'assure que tous les inserts ont été faits => on fait un flush
		entityManager.flush();

		// when: read from the repository
		var moviesFound = movieRepository.findByDurationNull();
		// then
		
		/**
		 * commentaires à ne pas laisser en prod
		 */
//		System.out.println(moviesDatabase);
//		System.out.println(moviesFound);
		
		
		assertEquals(4,moviesFound.size());
		assertAll(moviesFound.stream().map(m-> () -> assertTrue((Integer)(m.getDuration())==null ,"not such a null duration film")));
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
