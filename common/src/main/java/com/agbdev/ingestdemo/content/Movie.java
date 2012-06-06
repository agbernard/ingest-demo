package com.agbdev.ingestdemo.content;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement
public interface Movie {

	public Long getId();
	public void setId(long id);
	public String getName();
	public void setName(final String name);
}
