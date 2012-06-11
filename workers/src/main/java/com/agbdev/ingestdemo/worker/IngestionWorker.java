package com.agbdev.ingestdemo.worker;

import static com.agbdev.ingestdemo.QueueProperties.QUEUE_HOST;
import static com.agbdev.ingestdemo.QueueProperties.QUEUE_NAME;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.IOUtils;
import com.agbdev.ingestdemo.IngestTask;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class IngestionWorker
implements AutoCloseable {
	private final long id = Thread.currentThread().getId();
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
		channel.basicQos(1);

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, false, consumer);
		return consumer;
	}

	private void startExecutionLoop(final QueueingConsumer consumer)
	throws ShutdownSignalException, ConsumerCancelledException, InterruptedException, IOException {
		while (true) {
			System.out.println(String.format("[Worker %d] Waiting for messages ...", id));
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String taskString = new String(delivery.getBody());
			IngestTask task = new Gson().fromJson(taskString, IngestTask.class);

			System.out.println(String.format("[Worker %d] Received task: %s", id, taskString));

			doIngestion(task);
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

			System.out.println(String.format("[Worker %d] Done", id));
			System.out.println();
		}
	}

	private void doIngestion(final IngestTask task) {
		String supplierContent = getContent(task.getContentUrl());

		Class<?> transportClass = getTransportClass(task.getContentType());
		Object content = new Gson().fromJson(supplierContent, transportClass);
		System.out.println("Received content: "+ content);

		//TODO: need to persist OR update based on contentId; it is currently inserting a new row every time
		PersistenceUtil.persist(content);
		PersistenceUtil.list(transportClass);
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
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private Class<?> getTransportClass(final String contentType) {
		ContentType type = ContentType.valueOf(contentType.toUpperCase());
		return type.getTransportClass();
	}

	@Override
	public void close()
	throws Exception {
		if (connection != null && !connection.isOpen()) {
			connection.close();
		}

		if (channel != null && !channel.isOpen()) {
			channel.close();
		}
	}

}
