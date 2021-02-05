package org.bollywood.movieapp.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.repository.Temporal;

@Entity
@Table(name = "stars") // nom de la table dans postgresql
public class Artist {
private Integer id;
private String name; //required
private LocalDate birthdate;

private LocalDate deathdate;
public Artist(String name, LocalDate birthdate) {
	super();
	this.name = name;
	this.birthdate = birthdate;
}
public Artist(String name) {
	super();
	this.name = name;
}
public Artist() {
	super();
}
public Artist(String name, LocalDate birthdate, LocalDate deathdate) {
	super();
	this.name = name;
	this.birthdate = birthdate;
	this.deathdate = deathdate;
}
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getName() {
	return this.name;
}
public void setName(String name) {
	this.name = name;
}

//@Temporal(TemporalType.TIMESTAMP)
@Column(nullable = true)
public LocalDate getBirthdate() {
	return birthdate;
}
public void setBirthdate(LocalDate birthdate) {
	this.birthdate = birthdate;
}
public LocalDate getDeathdate() {
	return deathdate;
}
@Column(nullable = true)
public void setDeathdate(LocalDate deathdate) {
	this.deathdate = deathdate;
}
@Override
public String toString() {
	StringBuilder builder= new StringBuilder();
	builder.append(this.name)
	.append("[(")
	.append(birthdate)
	.append("-")
	.append(deathdate)
	.append(") #")
	.append(id)
	.append("]");
	return builder.toString();
}
}
