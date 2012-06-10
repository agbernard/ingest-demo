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
	public Movie getMovie(final @PathParam("id") Long contentId) {
		//TODO: get movie based on id

		return new Movie() {

			public void setName(final String name) {
				// not needed
			}

			public void setId(final long id) {
				// not needed
			}

			public String getName() {
				return "Gladiator";
			}

			public Long getId() {
				return contentId;
			}
		};
	}

}