package com.agbdev.ingestdemo;

import java.util.List;
import com.google.gson.Gson;

public class IngestBatch {
	private String supplierUrl;
	private List<String> contentIds;
	private String contentType;

	public void setSupplierUrl(final String supplierUrl) {
		this.supplierUrl = supplierUrl;
	}

	public String getSupplierUrl() {
		return supplierUrl;
	}

	public void setContentIds(final List<String> contentIds) {
		this.contentIds = contentIds;
	}

	public List<String> getContentIds() {
		return contentIds;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}