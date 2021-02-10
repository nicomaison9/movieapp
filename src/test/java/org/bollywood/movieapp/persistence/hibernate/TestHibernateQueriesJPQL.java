package org.bollywood.movieapp.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import org.bollywood.movieapp.dto.DirectorCount;
import org.bollywood.movieapp.dto.MovieByYear;
import org.bollywood.movieapp.dto.MovieStat;
import org.bollywood.movieapp.dto.NameYearTitle;
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
@AutoConfigureTestDatabase(replace = Replace.NONE) // do not replace database of app with h2
													// if annotation not given, takes H2 database (results may be null
class TestHibernateQueriesJPQL {

	@Autowired
	// TestEntityManager entityManager;
	EntityManager entityManager;

	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * ------------------testing JPQL-----------------
	 */

	@Test
	void test_select_all_as_list() {
		// select movie0_.id as id1_0_, movie0_.id_director as id_direc5_0_,
		// movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as
		// year4_0_
		// from movies movie0_
		TypedQuery<Movie> query = entityManager.createQuery(
				// "from Movie",
				"select m from Movie m", // on prend uniquement les attributs de l'entité Movie, pas les attributs de la
											// table de postgresql //éviter de faire select *, trop gourmand
				Movie.class);
		List<Movie> movies = query.getResultList();
		System.out.println("nb movies= " + movies.size());
	}

	@Test
	void test_select_all_as_stream() {
		entityManager.createQuery("select m from Movie m", Movie.class).getResultStream().limit(10)
				.forEach(System.out::println);

	}

	@Test
	void test_select_predicate() {
		entityManager.createQuery("select m from Movie m where m.year=2020", Movie.class).getResultStream().limit(10)
				.forEach(System.out::println);

	}

	@ParameterizedTest
	@ValueSource(ints = { 2019, 2020, 2021 })
	void test_select_where_year(int year) {
		System.out.println("Movies from year " + year);
		entityManager.createQuery("select m from Movie m where m.year=:year", Movie.class).setParameter("year", year)
				.getResultStream().limit(10).forEach(System.out::println);

	}

	@ParameterizedTest
	@CsvSource({ "50 States of Fright,2020", "Guardians of Life,2020" })
	void test_select_where_title_year(String title, int year) {
		System.out.println("Movies from title,year:" + year);
		entityManager.createQuery("select m from Movie m where m.year = :year and m.title = :title", Movie.class)
				.setParameter("year", year).setParameter("title", title).getResultStream().limit(10)
				.forEach(System.out::println);

	}

	@Test
	void test_select_where_year_birthdate() {
		entityManager.createQuery("select a from Artist a where extract(year from a.birthdate) = :year", Artist.class)
				.setParameter("year", 1930).getResultStream().limit(10).forEach(System.out::println);

	}

	// artists of age 30
	@Test
	void test_select_where_Artist_30year() {
		entityManager.createQuery(
				"select a from Artist a where (extract(year from CURRENT_DATE)-extract(year from a.birthdate)) = :age",
				Artist.class).setParameter("age", 30).getResultStream().limit(10).forEach(System.out::println);

	}

	@Test
	void test_select_movie_with_director_named() {
		entityManager.createQuery("select m from Movie m join m.director a where a.name= :name", Movie.class)
				.setParameter("name", "Clint Eastwood").getResultStream().forEach(System.out::println);

	}

	@Test
//	 select movie0_.id as id1_0_, movie0_.id_director as id_direc5_0_, movie0_.duration as duration2_0_, movie0_.title as title3_0_, movie0_.year as year4_0_ 
//	from movies movie0_ inner join play actors1_ on movie0_.id=actors1_.id_movie inner join stars artist2_ on actors1_.id_actor=artist2_.id where artist2_.name=?
//	 select artist0_.id as id1_2_0_, artist0_.birthdate as birthdat2_2_0_, artist0_.deathdate as deathdat3_2_0_, artist0_.name as name4_2_0_ 
//	 from stars artist0_ where artist0_.id=?
	void test_select_movie_with_actor_named() {
		entityManager.createQuery("select m from Movie m join m.actors a where a.name= :name", Movie.class)
				.setParameter("name", "Clint Eastwood").getResultStream().forEach(System.out::println);

	}

	@Test
	void test_movie_stat() {
		// count
		TypedQuery<Long> query = entityManager.createQuery("select count(*) from Movie m", Long.class);
		Long nb_movies = query.getSingleResult();
		System.out.println(nb_movies);

		// min(year)
		int min_year = entityManager.createQuery("select min(m.year) from Movie m", Integer.class).getSingleResult();
		System.out.println("year of first movie: " + min_year);

		// sum(duration) between year1 and year2
		long total_duration = entityManager
				.createQuery("select coalesce(sum(m.duration),0) from Movie m where m.year between :year1 and :year2",
						Long.class)
				// sans coalesce, result = null
				// avec coalesce, result = 0; renvoie la première valeur non nulle
				.setParameter("year1", 2021).setParameter("year2", 2029).getSingleResult();
		System.out.println("total duration : " + total_duration);

		// min(duration) between year1 and year2
		Optional<Integer> min_duration = Optional.ofNullable(entityManager
				.createQuery("select min(m.duration) from Movie m where m.year between :year1 and :year2",
						Integer.class) // type erasure
				.setParameter("year1", 2020).setParameter("year2", 2029).getSingleResult());
		System.out.println("min duration : " + min_duration);

	}

	@Test
	void test_movie_several_stats_as_object_array() {
		var res = entityManager.createQuery(
				// la notion de tuple n'exite pas en java, on est obligé de passer par un array
				"select count(*),min(m.year), max(m.year) from Movie m", Object[].class).getSingleResult();
		System.out.println("movie stats: " + Arrays.toString(res) + "(" + res.getClass() + ")");
		long nb_movies = (long) res[0];
		int min_year = (int) res[1];
		int max_year = (int) res[2];
		System.out.println(
				" nb de movies: " + nb_movies + "\n année min   : " + min_year + "\n année max   : " + max_year);

	}

	@Test
	void test_movie_several_stats_as_tuple() {
		var res = entityManager.createQuery(
				// la notion de tuple n'exite pas en java, on est obligé de passer par un array
				"select count(*),min(m.year), max(m.year) from Movie m", Tuple.class).getSingleResult();
		System.out.println("movie stats: " + res);
		long nb_movies = res.get(0, Long.class);
		int min_year = res.get(1, Integer.class);
		int max_year = res.get(2, Integer.class);
		System.out.println(
				" nb de movies: " + nb_movies + "\n année min   : " + min_year + "\n année max   : " + max_year);

	}

	@Test
	void test_movie_several_stats_as_dto() {
		var res = entityManager.createQuery("select count(*),min(m.year), max(m.year) from Movie m", MovieStat.class)
				.getSingleResult();
		System.out.println("movie stats: " + res);
		long nb_movies = res.getCount();
		int min_year = res.getMinYear();
		int max_year = res.getMaxYear();
		System.out.println(
				" nb de movies: " + nb_movies + "\n année min   : " + min_year + "\n année max   : " + max_year);

	}

	@Test
	void test_movie_several_stats_as_dto_bis() {
		// cadeau jdql: créer un objet dans la requête
		var res = entityManager.createQuery(
				"select new org.bollywood.movieapp.dto.MovieStat(count(*),min(m.year), max(m.year)) from Movie m",
				MovieStat.class).getSingleResult();
		System.out.println("movie stats: " + res);
		long nb_movies = res.getCount();
		int min_year = res.getMinYear();
		int max_year = res.getMaxYear();
		System.out.println(
				" nb de movies: " + nb_movies + "\n année min   : " + min_year + "\n année max   : " + max_year);

	}

	@Test
	void test_movie_projection() {
		String name = "John Wayne";
		List<NameYearTitle> res = entityManager.createQuery(
				"select  new org.bollywood.movieapp.dto.NameYearTitle( a.name,m.year,m.title) from Movie m join m.actors a where a.name like :name order by m.year",
				NameYearTitle.class).setParameter("name", name).getResultStream().limit(10)
				.collect(Collectors.toList());
		res.forEach(nyt -> System.out
				.println("name = " + nyt.getName() + " year = " + nyt.getYear() + " title= " + nyt.getTitle()));
	}

	// nb movies by year(params thresholdCount,thresholdYear) order by Year/count
	// desc
	@Test
	void test_movie_stat_by_year() {
		entityManager.createQuery("select m.year, count(*) from Movie m group by m.year", Object[].class)
				.getResultStream().limit(10).forEach(row -> System.out.println(Arrays.toString(row)));
	}

	// nb movies by year(params thresholdCount,thresholdYear) order by Year/count
	// desc
	@Test
	void test_movie_stat_by_year2() {
		entityManager
				.createQuery("select m.year, count(*) as nb_movies from Movie m group by m.year order by year desc ",
						Object[].class)
				.getResultStream().limit(10).forEach(row -> System.out.println(Arrays.toString(row)));
	}

	// nb movies by year(params thresholdCount,thresholdYear) order by Year/count
	// desc
	@Test
	void test_movie_stat_by_year3() {
		entityManager
				.createQuery("select new org.bollywood.movieapp.dto.MovieByYear(m.year,count(*) as nb_movies) from Movie m group by m.year order by year desc ",
						MovieByYear.class)
				.getResultStream().limit(10).forEach(row -> System.out.println("year = "+ row.getYear()+" count = "+row.getCount()));
	}

	// stats by director (count, min(year), max(year) order by count desc
	//TODO: a faire en array, tuple et en dto
	@Test
	void test_movie_stat_by_director() {
		
		String name = "Alfred Hitchcock"; //select count(*) as count,min(year) as yearmin,max(year) as yearmax from movies m join stars s on m.id_director=s.id order by count desc
		List<DirectorCount> res=  entityManager 
				.createQuery("select new org.bollywood.movieapp.dto.directorCount(count(*) as count,min(year) as yearmin,max(year) as yearmax from Movie m join m.director order by count(*) desc",
						DirectorCount.class)
				.getResultStream().limit(10).collect(Collectors.toList());
		res.forEach(d->System.out.println("count= "+d.getCount()+"yearmin= "+d.getYearmin()+"yearmax= "+d.getYearmax()));
		
	}
						// stats by actor (count, min(year), max(year) order by count desc
	//TODO: a faire en array, tuple et en dto
	
	//ambiguité
	void test_movie_ambigue() {
		int deltaYear=2;
		var res=entityManager
//		.createQuery("select m from Movies m where extract(year from current_date) - m.year <= :deltaYear",Movie.class)
//		pour pas de conflit, mettre en UPPERCASE
		.createQuery("select m from Movies m where EXTRACT(YEAR FROM CURENT_DATE) - m.year <= :deltaYear",Movie.class)
		.setParameter("deltaYear", deltaYear)
		.getResultList();
		System.out.println(res);
		
		
	}


}
