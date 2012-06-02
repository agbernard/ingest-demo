package com.agbdev.ingestdemo.producer;

import java.util.List;

public class IngestTask {
	private String supplierUrl;
	private List<String> contentIds;

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

	@Override
	public String toString() {
		return String.format("{url: %s, contentIds: %s}", supplierUrl, contentIds);
	}
}