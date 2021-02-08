package org.bollywood.movieapp.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.bollywood.movieapp.entity.Artist;
import org.bollywood.movieapp.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) //do not replace database of app with h2
													//if annotation not given, takes H2 database (results may be null
class TestHibernateQueries {

	@Autowired
	//TestEntityManager entityManager;
	EntityManager entityManager;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * ------------------testing JPQL-----------------
	 */
	
	@Test
	void test_select_all_as_list() {
		//		select movie0_.id as id1_0_, movie0_.id_director as id_direc5_0_, movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_ 
		//		from movies movie0_
		TypedQuery<Movie> query=entityManager.createQuery(
				//"from Movie",
				"select m from Movie m",  	//on prend uniquement les attributs de l'entité Movie, pas les attributs de la table de postgresql //éviter de faire select *, trop gourmand
				Movie.class);
		List<Movie> movies = query.getResultList();
		System.out.println("nb movies= "+movies.size());
	}

	@Test
	void test_select_all_as_stream() {
		entityManager.createQuery(
				"select m from Movie m",  	
				Movie.class)
				.getResultStream()
				.limit(10)
				.forEach(System.out::println);
		
	}
	@Test
	void test_select_predicate() {
		entityManager.createQuery(
				"select m from Movie m where m.year=2020",  	
				Movie.class)
				.getResultStream()
				.limit(10)
				.forEach(System.out::println);
		
	}
	@ParameterizedTest
	@ValueSource(ints= {2019,2020,2021})
	void test_select_where_year(int year) {
		System.out.println("Movies from year "+year);
		entityManager.createQuery(
				"select m from Movie m where m.year=:year",  	
				Movie.class)
				.setParameter("year",year)
				.getResultStream()
				.limit(10)
				.forEach(System.out::println);
		
	}
	@ParameterizedTest
	@CsvSource({"50 States of Fright,2020",
				"Guardians of Life,2020"})
	void test_select_where_title_year(String title, int year) {
		System.out.println("Movies from title,year:"+year);
		entityManager.createQuery(
				"select m from Movie m where m.year = :year and m.title = :title",  	
				Movie.class)
				.setParameter("year",year)
				.setParameter("title",title)
				.getResultStream()
				.limit(10)
				.forEach(System.out::println);
		
	}
	@Test
	void test_select_where_year_birthdate() {
		entityManager.createQuery(
				"select a from Artist a where extract(year from a.birthdate) = :year",  	
				Artist.class)
				.setParameter("year", 1930)
				.getResultStream()
				.limit(10)
				.forEach(System.out::println);
		
	}
	
	//artists of age 30
	@Test
	void test_select_where_Artist_30year() {
		entityManager.createQuery(
				"select a from Artist a where (extract(year from CURRENT_DATE)-extract(year from a.birthdate)) = :age",  	
				Artist.class)
				.setParameter("age", 30)
				.getResultStream()
				.limit(10)
				.forEach(System.out::println);
		
	}
	@Test
	void test_select_movie_with_director_named() {
		entityManager.createQuery(
				"select m from Movie m join m.director a where a.name= :name",  	
				Movie.class)
				.setParameter("name", "Clint Eastwood")
				.getResultStream()
				.forEach(System.out::println);
		
	}
	@Test
	void test_select_movie_with_actor_named() {
		entityManager.createQuery(
				"select m from Movie m join m.actors a where a.name= :name",  	
				Movie.class)
				.setParameter("name", "Clint Eastwood")
				.getResultStream()
				.forEach(System.out::println);
		
	}
}
