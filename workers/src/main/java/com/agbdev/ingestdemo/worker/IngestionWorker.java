package com.agbdev.ingestdemo.worker;

import static com.agbdev.ingestdemo.QueueProperties.QUEUE_HOST;
import static com.agbdev.ingestdemo.QueueProperties.QUEUE_NAME;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.IOUtils;
import com.agbdev.ingestdemo.content.Movie;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class IngestionWorker {
	private Connection connection;
	private Channel channel;

	public void start()
	throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		QueueingConsumer consumer = initChannelConsumer();
		startExecutionLoop(consumer);
	}

	private QueueingConsumer initChannelConsumer()
	throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(QUEUE_HOST);
		connection = factory.newConnection();
		channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null); // purposely non-durable just for the sake of the demo
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		channel.basicQos(1);

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, false, consumer);
		return consumer;
	}

	private void startExecutionLoop(final QueueingConsumer consumer)
	throws ShutdownSignalException, ConsumerCancelledException, InterruptedException, IOException {
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String contentUrl = new String(delivery.getBody());
			System.out.println("[x] Received ingestion content supplier: " + contentUrl);

			doIngestion(contentUrl);
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

			System.out.println("[x] Done");
			System.out.println();
		}
	}

	private void doIngestion(final String contentUrl) {
		String content = getContent(contentUrl);
		Movie movie = new Gson().fromJson(content, MovieTransport.class);
		System.out.println("Received content: "+ movie);

		System.out.println("TODO: ingest data to db");
	}

	private String getContent(final String contentUrl) {
		try {
			URL url = new URL(contentUrl);
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			return IOUtils.toString(in, encoding);
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void dispose()
	throws IOException {
		if (connection != null && !connection.isOpen()) {
			connection.close();
		}
		if (channel != null && !channel.isOpen()) {
			channel.close();
		}
	}

	public static void main(final String[] args)
	throws Exception {
		IngestionWorker worker = new IngestionWorker();
		try {
			worker.start();
		}
		finally {
			worker.dispose();
		}

	}
}
