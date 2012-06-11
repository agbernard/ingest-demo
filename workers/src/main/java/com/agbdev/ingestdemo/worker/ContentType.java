package com.agbdev.ingestdemo.worker;

public enum ContentType {
	MOVIE(MovieTransport.class)
	;

	private Class<?> transportClass;

	private ContentType(final Class<?> transportClass) {
		this.transportClass = transportClass;
	}

	public Class<?> getTransportClass() {
		return transportClass;
	}
}
