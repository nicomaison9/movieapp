package org.bollywood.movieapp.persistence;

import java.util.List;

import org.bollywood.movieapp.entity.Artist;
import org.bollywood.movieapp.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist,Integer>{
	List<Artist> findByName(String name);

}
