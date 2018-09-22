package com.maps.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.maps.model.SearchCriteria;
import com.maps.workhorse.PlaceServiceWorkhorse;

@RestController
public class SimpleController {
	@Autowired
	PlaceServiceWorkhorse placeServiceWorkhorse = new PlaceServiceWorkhorse();
	
	@PostMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public String index(@RequestBody SearchCriteria searchCriteria) {
		String searchResult = new String();	
		try {
			searchResult = placeServiceWorkhorse.search(searchCriteria);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return searchResult;
	}
}
