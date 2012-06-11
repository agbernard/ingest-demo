package com.agbdev.ingestdemo.supplier;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.agbdev.ingestdemo.content.Movie;

@Path("/content")
public class ContentSupplier {
	private static Map<Long, Movie> movies = new HashMap<Long, Movie>();
	static {
		long id = 0;
		initMovie(++id, "Gladiator");
		initMovie(++id, "The Matrix");
		initMovie(++id, "The Dark Knight");
		initMovie(++id, "Equilibrium");
		initMovie(++id, "Braveheart");
		initMovie(++id, "Troy");
		initMovie(++id, "The Shawshank Redemption");
		initMovie(++id, "300");
		initMovie(++id, "Ironman");
		initMovie(++id, "The Avengers");
	}

	/*
	 * Test with:
	 * curl -X GET http://localhost:8081/content/movies/{id}
	 */
	@GET
	@Path("/movies/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Movie getMovie(final @PathParam("id") Long contentId) {
		Movie movie;
		if(movies.containsKey(contentId)) {
			movie = movies.get(contentId);
		}
		else {
			movie = movies.get(1L);
		}
		return movie;
	}

	private static void initMovie(final Long id, final String name) {
		Movie movie = new Movie() {

			public void setName(final String name) {
				// not needed
			}

			public void setId(final long id) {
				// not needed
			}

			public String getName() {
				return name;
			}

			public Long getId() {
				return id;
			}
		};

		movies.put(id, movie);
	}

}