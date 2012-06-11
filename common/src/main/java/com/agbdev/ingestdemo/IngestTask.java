package com.agbdev.ingestdemo;

import com.google.gson.Gson;


public class IngestTask {
	private String supplierUrl;
	private String contentId;
	private String contentType;

	public IngestTask(final String supplierUrl, final String contentId, final String contentType) {
		this.supplierUrl = supplierUrl;
		this.contentId = contentId;
		this.contentType = contentType;
	}

	public String getSupplierUrl() {
		return supplierUrl;
	}

	public String getContentId() {
		return contentId;
	}

	public String getContentType() {
		return contentType;
	}

	public String getContentUrl() {
		return String.format("%s/%s", supplierUrl, contentId);
	}

	public byte[] getBytes() {
		return toString().getBytes();
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
