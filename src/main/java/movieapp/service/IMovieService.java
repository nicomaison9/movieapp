package org.bollywood.movieapp.service;

import java.util.List;

import org.bollywood.movieapp.dto.MovieSimple;

public class IMovieService {
	public MovieSimple add(MovieSimple movie);
	public List<MovieSimple> getAll();
}
