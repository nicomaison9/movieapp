package org.bollywood.movieapp.dto;

public class MovieByYear {
	public int year;
	public long count;
	public MovieByYear() {
		super();
	}
	public MovieByYear(int year, long count) {
		super();
		this.year = year;
		this.count = count;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "movieByYear [year=" + year + ", count=" + count + "]";
	}
	
	

}
