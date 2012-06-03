package com.agbdev.ingestdemo.producer;

import static com.agbdev.ingestdemo.QueueProperties.QUEUE_NAME;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.agbdev.ingestdemo.IngestTask;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

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
		try {
			addToQueue(task);
		}
		catch (IOException e) {
			return Response.status(500).entity(e.getMessage()).build();
		}

		String statusMsg = String.format("Ingest task has been queued: %s", task);
		String msg = String.format("%s%sTriggered by: %s",
						statusMsg,
						System.getProperty("line.separator"),
						request.getRemoteAddr());
		System.out.println(msg);
		return Response.status(201).entity(statusMsg).build();
	}

	private void addToQueue(final IngestTask task)
	throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = null;
		Channel channel = null;

		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null); //purposely non-durable just for the sake of the demo
			channel.basicPublish("", QUEUE_NAME, MessageProperties.MINIMAL_BASIC, task.getBytes());
		}
		finally {
			if (channel != null) {
				channel.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
}
