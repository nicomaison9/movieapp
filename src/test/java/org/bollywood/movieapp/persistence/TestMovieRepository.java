package org.bollywood.movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import org.bollywood.movieapp.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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

	@ParameterizedTest
	@ValueSource(strings= {
	"Z",
	"Blade Runner",
	"Night of the day of the down of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the of the"})
	void testSaveTitle(String title) {
		// given (hypothèses de départ
		
		int year = 1982;
		int duration = 175;
		saveAssertMovie(title, year, duration);
	}
	
	@Test
	void testSaveTitleEmptyNOK() {
		// given (hypothèses de départ
		String title=null;
		int year = 1982;
		int duration = 175;
		assertThrows(DataIntegrityViolationException.class,()->saveAssertMovie(title, year, duration));
	}

	private void saveAssertMovie(String title, int year, int duration) {
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
//		 .ifPresent((Movie m) -> assertEquals(movie,m));

//		 debug purpose only
//		System.out.println(movie);
	}

}
