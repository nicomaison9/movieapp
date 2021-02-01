package org.bollywood.movieapp.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestMovie {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testDefaultConstructor() {
		Movie movie = new Movie();
//		System.out.println(movie);
		assertAll(
				() -> assertNull(movie.getTitle(), "default movie title"),
				() -> assertEquals(null, movie.getDuration(), "default movie duration"),
				() -> assertEquals(null, movie.getYear(), "default movie year"));

	}
	@Test
	void testAllArgConstructor() {
		String title="mourir peut attendre";
		int year= 2021;
		int duration=173;
		Movie movie = new Movie(title, year, duration);
		assertAll(
				() -> assertEquals(title,movie.getTitle(), " movie title"),
				() -> assertEquals(duration, movie.getDuration(), " movie duration"),
				() -> assertEquals(year, movie.getYear(), " movie year"));
	}

}
