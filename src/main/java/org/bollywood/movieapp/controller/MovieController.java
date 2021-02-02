package org.bollywood.movieapp.controller;

import java.util.List;
import java.util.Optional;

import org.bollywood.movieapp.entity.Movie;
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
	 * @param title
	 * @return
	 */
	@GetMapping("byTitle")
	public List<Movie> moviesByTitle(@RequestParam("t") String title){
		return movieRepository.findByTitle(title);
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

}