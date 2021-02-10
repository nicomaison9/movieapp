package org.bollywood.movieapp.dto;

import org.bollywood.movieapp.entity.Artist;

public class DirectorCount {

	private Artist director;
	private Integer count;
	private Integer yearmin;
	private Integer yearmax;
	
	public DirectorCount(Artist director, Integer count, Integer yearmin, Integer yearmax) {
		super();
		this.director = director;
		this.count = count;
		this.yearmin = yearmin;
		this.yearmax = yearmax;
	}
	public DirectorCount() {
		super();
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getYearmin() {
		return yearmin;
	}
	public void setYearmin(Integer yearmin) {
		this.yearmin = yearmin;
	}
	public Integer getYearmax() {
		return yearmax;
	}
	public void setYearmax(Integer yearmax) {
		this.yearmax = yearmax;
	}
	@Override
	public String toString() {
		return "DirectorCount [director=" + director + ", count=" + count + ", yearmin=" + yearmin + ", yearmax="
				+ yearmax + "]";
	}

	
}
