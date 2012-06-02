package com.agbdev.ingestdemo.producer;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ingestion")
public class IngestTaskProducer {
	@Context private HttpServletRequest request;

	/*
	 * Test with:
	 * curl -H "Content-type: application/json" -X POST -d '{"supplierUrl": "myurl", "contentIds": ["1", "2"]}' http://localhost:8080/ingestion/tasks
	 */
	@POST
	@Path("/tasks")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ingest(final IngestTask task) {
		//TODO: send to queue

		String statusMsg = String.format("Ingest task has been queued: %s", task);
		String msg = String.format("%s%sTriggered by: %s",
						statusMsg,
						System.getProperty("line.separator"),
						request.getRemoteAddr());
		System.out.println(msg);
		return Response.status(201).entity(statusMsg).build();
	}
}
