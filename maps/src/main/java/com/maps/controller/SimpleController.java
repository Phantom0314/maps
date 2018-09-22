package com.maps.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.maps.model.SearchCriteria;
import com.maps.workhorse.PlaceServiceWorkhorse;

@RestController
public class SimpleController {
	private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);
	
	@Autowired
	PlaceServiceWorkhorse placeServiceWorkhorse = new PlaceServiceWorkhorse();
	
	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView index(String jsonLocation) {
		JSONObject jsonObject = new JSONObject(jsonLocation);
		final String latitude = jsonObject.getString("latitude");
		final String longitude = jsonObject.getString("longitude");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setKeyword("bar");
		searchCriteria.setLatitude(Integer.parseInt(latitude));
		searchCriteria.setLongitude(Integer.parseInt(longitude));
		try {
			String searchResult = placeServiceWorkhorse.search(searchCriteria);
			//model.addObject("searchResult", searchResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}
}
