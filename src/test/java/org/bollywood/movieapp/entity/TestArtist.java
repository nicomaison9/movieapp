package org.bollywood.movieapp.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestArtist {

	@BeforeEach
	void setUp() throws Exception {
	}

//TODO: unit tests of the java bean
	@Test
	void testConstructors() {
		var artists = List.of(
				new Artist("steve mc queen", LocalDate.of(1930, 3, 24), LocalDate.of(1980, 11, 7)),
				new Artist("alfred hitchcock", LocalDate.of(1930, 3, 24)),
				new Artist("dwayne johnson"));
		System.out.println(artists);

	}

}
