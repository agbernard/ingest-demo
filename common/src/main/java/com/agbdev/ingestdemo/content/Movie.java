package com.agbdev.ingestdemo.content;

import javax.xml.bind.annotation.XmlRootElement;
import com.google.gson.Gson;

@SuppressWarnings("restriction")
@XmlRootElement
public class Movie {
	private String name;

	public Movie() {}

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
