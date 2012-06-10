package com.agbdev.ingestdemo.worker;


public class IngestionWorkerPool {

	public static void main(final String[] args)
	throws Exception {
		int numWorkersToStart = Integer.parseInt(args[0]);
		System.out.println(String.format("[*] Starting %d workers - press CTRL+C to close all.", numWorkersToStart));
		for(int i = 0; i < numWorkersToStart; i++) {
			startNewWorkerThread();
		}
	}

	private static void startNewWorkerThread() {
		//TODO: I know, I know, use thread pooling - this is just for the quick and dirty demo
		new Thread(new Runnable() {
			@Override
			public void run() {
				try(IngestionWorker worker = new IngestionWorker()) {
					worker.start();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
