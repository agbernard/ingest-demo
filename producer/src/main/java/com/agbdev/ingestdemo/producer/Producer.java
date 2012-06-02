package com.agbdev.ingestdemo.producer;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ingest")
public class Producer {

	@POST
	@Path("/task")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ingest(final IngestTask task) {

		String msg = "Received ingest task: " + task;
		System.out.println(msg);
		return Response.status(201).entity(msg).build();
	}
}
