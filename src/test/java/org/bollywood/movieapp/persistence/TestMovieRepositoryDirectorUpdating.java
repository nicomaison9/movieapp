package org.bollywood.movieapp.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;

import org.bollywood.movieapp.entity.Artist;
import org.bollywood.movieapp.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class TestMovieRepositoryDirectorUpdating {

	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	ArtistRepository artistRepository;
	
	@Autowired
//	EntityManager entityManager;
	TestEntityManager entityManager;
	
	@BeforeEach
	void setUp() throws Exception {
	}
	

	@Test
	void testSaveMovieWithDirector() {
		Artist clint = new Artist("Clint Eastwood",LocalDate.of(1930, 5,31));
		artistRepository.save(clint); //pas nécessaire si @ManyToOne(cascade=CascadeType.PERSIST)sur getDirector()
		Movie movie =new Movie("Unforgiven",1992,130);
		movie.setDirector(clint);
		movieRepository.save(movie);
		
		System.out.println("Write: "+movie+" with director: "+movie.getDirector());
		
		entityManager.clear();
//		select movie0_.id as id1_1_0_, movie0_.id_director as id_direc5_1_0_, movie0_.duration as duration2_1_0_, movie0_.title as title3_1_0_, movie0_.year as year4_1_0_, artist1_.id as id1_0_1_, artist1_.birthdate as birthdat2_0_1_, artist1_.deathdate as deathdat3_0_1_, artist1_.name as name4_0_1_ 
//		from movie movie0_ left outer join artist artist1_ on movie0_.id_director=artist1_.id 
//		where movie0_.id=?
		Movie movieRead = entityManager.find(Movie.class, movie.getId());
		System.out.println("Read: "+movieRead+" with director: "+movie.getDirector());
		assertNotNull(movieRead.getDirector());
	}
	

	@Test
	void testSetDirectorWithExistingMovieAndArtist() {
		//write data in database
		Artist artist = new Artist("Clint Eastwood",LocalDate.of(1930, 5,31));
		artistRepository.save(artist); //pas nécessaire si @ManyToOne(cascade=CascadeType.PERSIST)sur getDirector()
		Movie movie =new Movie("Unforgiven",1992,130);
		
		entityManager.persist(artist);
		entityManager.persist(movie);
		entityManager.flush(); // activer la synchronisation
		
		var idArtist = artist.getId();
		var idMovie = movie.getId();
		entityManager.clear();// vider le cache hibernate
		//read movie and artist from database
		var optartistRead = artistRepository.findById(idArtist);
		var optmovieRead = movieRepository.findById(idMovie);
		assertTrue(optartistRead.isPresent());
		assertTrue(optmovieRead.isPresent());
		var artistRead= optartistRead.get();
		var movieRead= optmovieRead.get();
		System.out.println("Read: "+artistRead);
		System.out.println("Read: "+movieRead+ " with director "+ movieRead.getDirector());
		// set association
		movieRead.setDirector(artistRead);
		//synchronize Jpa Repository
		//update movie set id_director=?, duration=?, title=?, year=? where id=?_
		entityManager.flush(); // activer la synchronisation
		//TODO: empty cache and read again data to check director association
		
		
		
		
		movie.setDirector(artist);
	}
}
