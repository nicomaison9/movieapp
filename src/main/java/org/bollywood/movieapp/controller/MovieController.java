package org.bollywood.movieapp.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.bollywood.movieapp.entity.Artist;
import org.bollywood.movieapp.entity.Movie;
import org.bollywood.movieapp.persistence.ArtistRepository;
import org.bollywood.movieapp.persistence.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Transactional // tout composant voulant utiliser un repository en mode transactionnel(ie.
				// services)
@RestController
@RequestMapping("/api/movies")
public class MovieController {

	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private ArtistRepository artistRepository;

	/**
	 * on rentre avec localhost:8080/api/movies
	 * 
	 * @return
	 */
	@GetMapping
	@ResponseBody
	public List<Movie> movies() {
//		return List.of(new Movie("blade runner",1982,175),
//				new Movie("kabir singh",2019,173));
		return movieRepository.findAll();
	}

	/**
	 * on rentre avec localhost:8080/api/movies/un
	 * 
	 * @return
	 */
	@GetMapping("/{id}")
	@ResponseBody
	public Optional<Movie> movie(@PathVariable("id") int id) {
//		return new Movie("Avatar",2009,162);
		return movieRepository.findById(id);

	}

	/**
	 * path /api/movies/byTitle?t=Spectre
	 * 
	 * @param title
	 * @return
	 */
	@GetMapping("byTitle")
	public List<Movie> moviesByTitle(@RequestParam("t") String title) {
		return movieRepository.findByTitle(title);
	}

	/**
	 * http://localhost:8080/api/movies/byTitleYear?t=titanic&y=2010
	 * 
	 * @param title
	 * @param year
	 * @return
	 */
	@GetMapping("/byTitleYear")
	public List<Movie> moviesByTitle(@RequestParam("t") String title,
//			@RequestParam(value="y", defaultValue = 2020) int year)
			@RequestParam(value = "y", required = false) Integer year) {
		if (Objects.isNull(year)) {
			return movieRepository.findByTitle(title);
		} else {
			return movieRepository.findByTitleContainingAndYearEquals(title, year);
		}
	}

	/**
	 * http://localhost:8080/api/movies/byYearBetween?y1=2000&y2=2009
	 * 
	 * @param yearmin
	 * @param yearmax
	 * @return
	 */
	@GetMapping("/byYearBetween")
	public List<Movie> moviesbyyearbetween(@RequestParam(value = "y1", defaultValue = "2000") int yearmin,
			@RequestParam(value = "y2", defaultValue = "2010") int yearmax) {
		if (Objects.nonNull(yearmin)) {
			if (Objects.nonNull(yearmax)) {
				return movieRepository.findByYearBetweenOrderByTitleAsc(yearmin, yearmax);
			}
		} else if (Objects.nonNull(yearmax)) {
			return movieRepository.findByYearGreaterThanEqual(yearmin);
				} else {
						return List.of();
					}
		return List.of();

	}



	@PostMapping
	@ResponseBody
	public Movie addMovie(@RequestBody Movie movie) {
		// TODO: persist movie object
		movieRepository.save(movie); // insert movie
		System.out.println(movie);
		return movie;

	}

	@PutMapping
	public Optional<Movie> updateMovie(@RequestBody Movie movie) {
		// read movie from database/repository
		Optional<Movie> optmovieDb = movieRepository.findById(movie.getId());
		optmovieDb.ifPresent(m -> {
			m.setTitle(movie.getTitle());
			m.setYear(movie.getYear());
			m.setDuration(movie.getDuration());
//			movieRepository.flush();
		});
		// TODO: persist movie object
		return optmovieDb;
	}

	@DeleteMapping
	public Optional<Movie> deleteMovie(@RequestBody Movie movie) {
		return deleteMoviebyId(movie.getId());

	}

	/**
	 * 
	 * url /api/movies/1
	 * 
	 */
	@DeleteMapping("/{id}")
	public Optional<Movie> deleteMoviebyId(@PathVariable("id") int id) {
		Optional<Movie> optmovieDb = movieRepository.findById(id);
		optmovieDb.ifPresent(m -> movieRepository.deleteById(m.getId()));
		// TODO: persist movie object
		return optmovieDb;
	}
	
	/**
	 * path /api/movies/director?mid=1&did=3
	 * @param idMovie
	 * @param idDirector
	 * @return
	 */
	@PutMapping("/director")
	public Optional<Movie> setDirector(
			@RequestParam("mid") int idMovie,
			@RequestParam("did") int idDirector){
		//chercher Movie et artist correspondant à idmovie et iddirector
		
		Optional<Movie> optmovie = movieRepository.findById(idMovie);
		Optional<Artist> optartist = artistRepository.findById(idDirector);
		//		si ok*2
		if (optartist.isPresent() && optmovie.isPresent())
		{
			Movie movie=optmovie.get();
			Artist artist= optartist.get();
			movie.setDirector(artist);
			return Optional.of(movie);
		}
		else	return Optional.empty();
	}
	/**
	 * path /api/movies/director2?mid=1&did=3
	 * @param idMovie
	 * @param idDirector
	 * @return
	 */
	@PutMapping("/director2")
	public Optional<Movie> setDirector2(
			@RequestParam("mid") int idMovie,
			@RequestParam("did") int idDirector){
//		movie:	 select movie0_.id as id1_1_0_, movie0_.id_director as id_direc5_1_0_, movie0_.duration as duration2_1_0_, movie0_.title as title3_1_0_, movie0_.year as year4_1_0_, artist1_.id as id1_0_1_, artist1_.birthdate as birthdat2_0_1_, artist1_.deathdate as deathdat3_0_1_, artist1_.name as name4_0_1_ from movie movie0_ left outer join artist artist1_ on movie0_.id_director=artist1_.id where movie0_.id=?
//		artist:	 select artist0_.id as id1_0_0_, artist0_.birthdate as birthdat2_0_0_, artist0_.deathdate as deathdat3_0_0_, artist0_.name as name4_0_0_ from artist artist0_ where artist0_.id=?
//		director:update movie set id_director=?, duration=?, title=?, year=? where id=?
		
		return movieRepository.findById(idMovie)
				.flatMap(m-> artistRepository.findById(idDirector)
						.map(a->{m.setDirector(a);return m;}));
		
		
	}
}
