package com.agbdev.ingestdemo.worker;

import javax.xml.bind.annotation.XmlRootElement;
import com.agbdev.ingestdemo.content.Movie;
import com.google.gson.Gson;

@SuppressWarnings("restriction")
@XmlRootElement
public class MovieTransport implements Movie {
	private long id;
	private String name;

	public MovieTransport() {}

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
