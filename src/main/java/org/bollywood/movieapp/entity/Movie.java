package org.bollywood.movieapp.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

//POJO: plain old java object
//Bean
@Entity
public class Movie {
	// implicit default constructor

	private Integer id;
	private String title;
	private Integer year;
	private Integer duration;
	private Artist director;
	private List<Artist> actors;
	

	


	public Movie() {
		actors=new ArrayList<>();}
	
	public Movie(String title, Integer year, Integer duration) {
		this();
		this.title = title;
		this.year = year;
		this.duration = duration;
	}
	//transient = opposé de persistant=  à ne pas sauvegarder
	//@Transient
	//(cascade=CascadeType.PERSIST) sauvegarde des objets en cascade
	//	@ManyToOne(cascade=CascadeType.PERSIST)
	@ManyToOne
	@JoinColumn(name="id_director",nullable=true)
	public Artist getDirector() {
		return director;
	}

	public void setDirector(Artist director) {
		this.director = director;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	@Column(nullable = false,length=300)
	public	String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false)
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Column(nullable = true)
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@ManyToMany
	public List<Artist> getActors() {
		return actors;
	}

	public void setActors(List<Artist> actors) {
		this.actors = actors;
	}

	@Override
	public String toString() {
//		return "Movie [title=" + title + " ( year=" + year + ", duration=" + duration + ")]";
		StringBuilder builder = new StringBuilder();
		return builder.append(title).append("[(").append(year).append(",").append(duration).append(" min)").append(" #").append(id).append("]").toString();
		

	}

}
