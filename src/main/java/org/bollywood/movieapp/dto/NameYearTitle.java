package org.bollywood.movieapp.dto;

public class NameYearTitle {
	 //a.name,m.year,m.title 
	 private String name;
	 private int year;
	 private String title;
	public NameYearTitle(String name, int year, String title) {
		super();
		this.name = name;
		this.year = year;
		this.title = title;
	}
	public NameYearTitle() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "NameYearTitle [name=" + name + ", year=" + year + ", title=" + title + "]";
	}
	 

}
