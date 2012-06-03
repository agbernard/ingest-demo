package com.agbdev.ingestdemo.worker;

import static com.agbdev.ingestdemo.QueueProperties.QUEUE_NAME;
import java.io.IOException;
import com.agbdev.ingestdemo.IngestTask;
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
		factory.setHost("localhost");
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
			String jsonString = new String(delivery.getBody());
			System.out.println("[x] Received " + jsonString);

			IngestTask task = new Gson().fromJson(jsonString, IngestTask.class);
			doIngestion(task);
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

			System.out.println("[x] Done");
		}
	}

	private void doIngestion(final IngestTask task) {
		System.out.println("TODO: do ingestion");
	}

	public void dispose()
	throws IOException {
		if (connection != null) {
			connection.close();
		}
		if (channel != null) {
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
