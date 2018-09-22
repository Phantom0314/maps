package com.maps.model;

import java.io.Serializable;

public class Place implements Serializable {

	private static final long serialVersionUID = -7254072371296537884L;
	
	private String name;
	private String reference;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}	
}
