package org.bollywood.movieapp.persistence;

import org.bollywood.movieapp.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

//integer en fonction du nombre d'éléments à gérer
//long si besoin de d'avantage d'éléments
//by default use database in memory H2
//
public interface MovieRepository extends JpaRepository<Movie, Integer>{

}
