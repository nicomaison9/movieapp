package org.bollywood.movieapp.persistence;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.bollywood.movieapp.dto.NameYearTitle;
import org.bollywood.movieapp.dto.INameYearTitle;
import org.bollywood.movieapp.entity.Artist;
import org.bollywood.movieapp.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArtistRepository extends JpaRepository<Artist,Integer>{
//	List<Artist> findByName(String name);
	Set<Artist> findByNameIgnoreCase(String Name); 			//liste non triée
	Stream<Artist> findByNameEndingWithIgnoreCase(String name); //à préférer à la liste
	
	@Query("select a from Artist a where EXTRACT(YEAR FROM a.birthdate) = :year")
	Stream<Artist> findByBirthdateYear(int year);
	
	@Query("select  new org.bollywood.movieapp.dto.NameYearTitle( a.name,m.year,m.title) "
			+ "from Movie m join m.actors a where a.name like :name order by m.year")
	Stream<NameYearTitle> filmography(String name); 
	
	
	//remplit pas les champs si on met pas les "as"(alias)
	@Query("select  a.name as name, m.year as year, m.title as title from Movie m join m.actors a where a.name like :name order by m.year")
	Stream<INameYearTitle> filmography2(String name); 
	
}
