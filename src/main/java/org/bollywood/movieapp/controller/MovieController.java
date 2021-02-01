package org.bollywood.movieapp.controller;

import java.util.List;
import java.util.Optional;

import org.bollywood.movieapp.entity.Movie;
import org.bollywood.movieapp.persistence.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
	
	@Autowired
	private MovieRepository movieRepository;
	/**
	 * on rentre avec localhost:8080/api/movies
	 * @return
	 */
	@GetMapping
	@ResponseBody
	public List<Movie>   movies() {
//		return List.of(new Movie("blade runner",1982,175),
//				new Movie("kabir singh",2019,173));
		return movieRepository.findAll();
	}
	
	/**
	 * on rentre avec localhost:8080/api/movies/un
	 * @return
	 */
	@GetMapping("/{id}")
	@ResponseBody
	public Optional<Movie>  movie(@PathVariable("id") int id){
//		return new Movie("Avatar",2009,162);
		return movieRepository.findById(id);
		
	}
	
	
	@PostMapping
	@ResponseBody
	public Movie addMovie(@RequestBody Movie movie) {
		//TODO: persist movie object
		movieRepository.save(movie); //insert movie
		System.out.println(movie);
		return movie;
		
	}
}
