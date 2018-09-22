package com.maps.workhorse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.maps.model.SearchCriteria;

@Service
public class PlaceServiceWorkhorse {

	static Logger logger = Logger.getLogger(PlaceServiceWorkhorse.class.toString());
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_SEARCH = "/nearbysearch";
	private static final String OUT_JSON = "/json?";
	
	private static final String API_KEY = "AIzaSyDVJuxExkq9Ks9Tc-gxvdaUZhS5I4btYl4";
	
	public String search(SearchCriteria searchCriteria) throws Exception {
		HttpURLConnection httpURLConnection = null;
		StringBuilder jsonResults = new StringBuilder();
		
		try {
			
			StringBuilder urlBuilder = new StringBuilder(PLACES_API_BASE);
			urlBuilder.append(TYPE_SEARCH);
			urlBuilder.append(OUT_JSON);
			urlBuilder.append("&location=" + String.valueOf(searchCriteria.getLatitude()) + "," + String.valueOf(searchCriteria.getLongitude()));
			urlBuilder.append("&radius=800");
			urlBuilder.append("&type=" + searchCriteria.getKeyword());
			urlBuilder.append("&key=" + API_KEY);
			
			URL url = new URL(urlBuilder.toString());
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
			
			int read;
			char[] buffer = new char[1024];
			while ((read = inputStreamReader.read(buffer)) != -1) {
				jsonResults.append(buffer, 0, read);
			}
			
		} catch (MalformedURLException exception) {
			throw(exception);
		} catch (IOException exception) {
			throw(exception);
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return jsonResults.toString();
	}
}
