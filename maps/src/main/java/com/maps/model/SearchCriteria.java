package com.maps.model;

import java.io.Serializable;

public class SearchCriteria implements Serializable {

	private static final long serialVersionUID = 7702251968438283619L;
	
	private String keyword;
	private String latitude;
	private String longitude;
	
	public SearchCriteria() {}
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
