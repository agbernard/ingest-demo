package com.agbdev.ingestdemo.worker;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import com.agbdev.ingestdemo.content.Movie;
import com.google.gson.Gson;

@Entity
@Table(name="Movies")
public class MovieTransport implements Movie {
	private long id;
	private String name;

	public MovieTransport() {}

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public Long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
