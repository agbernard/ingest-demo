package com.agbdev.ingestdemo.worker;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.agbdev.ingestdemo.content.Movie;
import com.google.gson.Gson;

@Entity
@Table(name="Movies")
public class MovieTransport implements Movie {
	private long dbId;
	private long id;
	private String name;

	public MovieTransport() {}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getDbId() {
		return dbId;
	}

	public void setDbId(final long dbId) {
		this.dbId = dbId;
	}

	/*
	 * TODO: This is the ID from the supplier - ideally it would be named more appropriately while still allowing
	 * auto-marshalling from the "id" field of the Json format
	 */
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}