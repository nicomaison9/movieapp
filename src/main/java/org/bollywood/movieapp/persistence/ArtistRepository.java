package org.bollywood.movieapp.persistence;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.bollywood.movieapp.entity.Artist;
import org.bollywood.movieapp.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist,Integer>{
//	List<Artist> findByName(String name);
	Set<Artist> findByNameIgnoreCase(String Name); 			//liste non triée
	Stream<Artist> findByNameEndingWithIgnoreCase(String name); //à préférer à la liste
	
}
