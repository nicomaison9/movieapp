package org.bollywood.movieapp.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.bollywood.movieapp.persistence.ArtistRepository;
import org.bollywood.movieapp.persistence.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TestSpringQueries {

	@Autowired
	// TestEntityManager entityManager;
	EntityManager entityManager;
	
	@Autowired
	ArtistRepository artistRepository;
	
	@Autowired
	MovieRepository movieRepository;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test_artists_by_birthdate_year() {
		int year=1930;
		artistRepository.findByBirthdateYear(year)
			.limit(10)
			.forEach(System.out::println);
	}
	
	@ParameterizedTest
	@CsvSource({"2010,2019","2030,2039"})

	void test_total_duration_range_year(int yearmin,int yearmax) {
//		int year1=2010;
//		int year2=2019;
		var res=movieRepository.totalDuration(yearmin,yearmax);
		System.out.println(res);
			
	}
	
	@ParameterizedTest
	@CsvSource({"2010,2019","2030,2039"})
	@Test
	void test_average_duration_range_year(int yearmin,int yearmax) {
//		int year1=2010;
//		int year2=2019;
		var res=movieRepository.averageDuration(yearmin,yearmax);
		System.out.println(res);
	}
	@ParameterizedTest
//	@CsvSource({"2010,2019","2030,2039"})
	@MethodSource("rangeYearSource")
	void test_total_duration_range_year2(int yearmin,int yearmax) {
//		int year1=2010;
//		int year2=2019;
		var res=movieRepository.totalDuration(yearmin,yearmax);
		System.out.println(res);
			
	}
	
	@ParameterizedTest
//	@CsvSource({"2010,2019","2030,2039"})
	@MethodSource("rangeYearSource")
	void test_average_duration_range_year2(int yearmin,int yearmax) {
//		int year1=2010;
//		int year2=2019;
		var res=movieRepository.averageDuration(yearmin,yearmax);
		System.out.println(res);
	}
	static Stream<Arguments> rangeYearSource() {
		return Stream.of(
				Arguments.arguments(2010.2019),
				Arguments.arguments(2030.2039));
				
	}
	@Test
	void test_statistics_dto() {
		var stats=movieRepository.statistics();
		long nb_movies = stats.getCount();
		int min_year = stats.getMinYear();
		int max_year = stats.getMaxYear();
		System.out.println(
				" nb de movies: " + nb_movies + "\n année min   : " + min_year + "\n année max   : " + max_year);
	}
	
	@Test
	void test_filmography_dto() {
		String name="Clint Eastwood";
		artistRepository.filmography(name)
			.forEach(nyt -> System.out
				.println("name = " + nyt.getName() + " year = " + nyt.getYear() + " title= " + nyt.getTitle()));
		
	}
	@Test
	void test_filmography2_dto() {
		String name="Clint Eastwood";
		artistRepository.filmography2(name)
			.forEach(nyt -> System.out
				.println("name = " + nyt.getName() + "; year = " + nyt.getYear() + "; title= " + nyt.getTitle()+"; class= "+nyt.getClass()));
		
	}
	}
