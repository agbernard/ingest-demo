package com.agbdev.ingestdemo.supplier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.agbdev.ingestdemo.content.Movie;

@Path("/content")
public class ContentSupplier {

	/*
	 * Test with:
	 * curl -X GET http://localhost:8081/content/movies/{id}
	 */
	@GET
	@Path("/movies/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Movie getMovie(final @PathParam("id") String contentId) {
		//TODO: get movie based on id
		Movie movie = new Movie();
		movie.setName("Gladiator");
		return movie;
	}

}