package org.bollywood.movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.bollywood.movieapp.entity.Artist;
import org.bollywood.movieapp.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class TestMovieRepositoryDirectorFind {

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	TestEntityManager entityManager;

	Movie movieH;
	Movie movieA;

	@BeforeEach
	void initData() throws Exception {
		var todd = new Artist("Todd philips", LocalDate.of(1970, 12, 20));
		var clint = new Artist("Clint Eastwood", LocalDate.of(1970, 12, 20));
		entityManager.persist(todd);
		entityManager.persist(clint);
		var moviesTodd = List.of(new Movie("the hangover", 2000, 150), new Movie("joker", 2019, 150),
				new Movie("a star is born", 2018, 150), new Movie("the hangover", 2008, 150),
				new Movie("limitless", 2016, 150), new Movie("project x", 2012, 150),
				new Movie("very bad trip2", 2011, 150));
		moviesTodd.forEach(m -> m.setDirector(todd));
		movieH = new Movie("unforgiven", 2012, 150);
		movieH.setDirector(clint);
		movieA = new Movie("alien", 1979, 150);
		// on veut pas dire que alien c'est ridley scott

		moviesTodd.forEach(entityManager::persist);
		entityManager.persist(movieH);
		entityManager.persist(movieA);
		entityManager.flush();
		entityManager.clear();

	}

	@Test
	void testFindMovieWithExistinDirector() {
		int idMovie = movieH.getId();
		var optMovie = movieRepository.findById(idMovie);
		// check movie read has a director
		assertNotNull(optMovie.get().getDirector());

	}

	@Test
	void testFindMovieWithNoDirector() {
		int idMovie = movieA.getId();
		var optMovie = movieRepository.findById(idMovie);
		// check movie read has a director
		assertNull(optMovie.get().getDirector());

		optMovie.ifPresent(m -> assertNull(m.getDirector()));
	}

	@Test
	void testFindByDirector() {
		//given
		String name = "Todd philips";
		//when
		var moviesFound = movieRepository.findByDirectorName(name);
		System.out.println(moviesFound);
		// TODO: vérifier le nombre de retours
		// verify all directed by todd par assert
		assertEquals(7,moviesFound.size(),"number movies");
		assertAll(
				moviesFound.stream()
				.map(Movie::getDirector)
				.map(Artist::getName)
				.map(n -> () -> assertEquals(name, n, "director name")));
	}

}
