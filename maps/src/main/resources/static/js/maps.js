$( document ).ready(function() {
	initMap();
});

var map, infoWindow;
/*
 * Build the map and gets the user's location
 * via IP Address
 */
function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: -34.397, lng: 150.644},
    zoom: 14
  });
  infoWindow = new google.maps.InfoWindow;
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
      var pos = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };

      infoWindow.setPosition(pos);
      infoWindow.setContent('Location found.');
      infoWindow.open(map);
      map.setCenter(pos);
      map.setOptions({draggable: false, zoomControl: false, scrollwheel: false, disableDoubleClickZoom: true});
      findLocalWateringHoles(pos);
    }, function() {
      handleLocationError(true, infoWindow, map.getCenter());
    });
  } else {
    handleLocationError(false, infoWindow, map.getCenter());
  }
}

/*
 * Handles the errors if Geolocation fials
 */
function handleLocationError(browserHasGeolocation, infoWindow, pos) {
  infoWindow.setPosition(pos);
  infoWindow.setContent(browserHasGeolocation ?
                        'Error: The Geolocation service failed.' :
                        'Error: Your browser doesn\'t support geolocation.');
  infoWindow.open(map);
}

/*
 * AJAX request that passes the user's longtiude
 * and latitude to be used to find bars in the area
 */
function findLocalWateringHoles(position) {
	var location = {
			"latitude" : position.lat,
			"longitude" : position.lng
	}
	
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/search",
		data : JSON.stringify(location),
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			console.log("SUCCESS: ", data);
			drop(data);
		},
		error : function(e) {
			console.log("ERROR: ", e);
		}
	});
}

var allMarkers = [];
var labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
var labelIndex = 0;
var places = [];
/*
 * Checks to make sure the service returned with a 200 status
 * if so then creates a place object and then build 
 * result information list
 */
function drop(data) {
	if ("OK" === data.status) {
		var results = data.results;
		for (var i = 0; i < results.length; i++) {;
			buildPlacesObject(results[i], i);
		}
		buildSearchResultInformationList();
	}
}

/*
 * Builds place object that contains pertinent information
 * about local bars
 */
function buildPlacesObject(position, index) {
	place = new Object ({
		locationName: position.name,
	    location: position.geometry.location,
	    rating: position.rating,
	    address: position.vicinity, 
	    label: labels[labelIndex++ % labels.length],
	    openNow: position.opening_hours
	});
	places.push(place);
	addMarkerWithTimeout(place, index * 200);
}

/*
 * Add's the bar markers to the map
 */
function addMarkerWithTimeout(place, timeout) {
	var latLng = new google.maps.LatLng(place.location);
	var contentString = "<div id='content'>" + place.locationName + "<br/> " + place.address + "<br/> Rating: " + place.rating + "</div>";
	window.setTimeout(function() {
		var infoWindow = new google.maps.InfoWindow({
			content: contentString
        });
		var marker = new google.maps.Marker({
			position: latLng,
			label: labels[labelIndex++ % labels.length],
			map: map,
			title: place.locationName,
			animation: google.maps.Animation.DROP
		});
		marker.addListener('click', function() {
			infoWindow.open(map, marker);	
		});
		allMarkers.push(marker)
    }, timeout);
}

/*
 * Builds the search results information list beneath the map
 */
function buildSearchResultInformationList() {
	if (places.length > 0) {
		var containerDiv = "<div class='locationContainer'>";
		var locationDiv = "";
		for (var i = 0; i < places.length; i++) {
			var currentPlace = places[i];
			var locationName = currentPlace.locationName;
			var rating = (typeof currentPlace.rating === "undefined" ? "No Ratings" : currentPlace.rating);
			var address = currentPlace.address;
			var label = currentPlace.label;
			
			locationDiv += "<div class='locationDiv'>" + 
								"<div class='locationLabel'>" + label + "</div>" +
								"<div class='locationInfo'>" + locationName + 
									"<div>" + address + "</div>" +
								"</div>" +
								"<div>Overall Rating: " + rating + "</div>" +
						  "</div>";
		}
		var resultInformationList = containerDiv + locationDiv + "</div>";
		$(".resultList").append(resultInformationList);
		
		attachResultListStyle();
	}
}

/*
 * Attaches styles to the results list
 */
function attachResultListStyle() {
	$(".locationContainer").css({
		'display': 'flex',
		'flex-wrap': 'wrap',
		'padding-top': '10px'
	});
	$(".locationDiv").css({
		'width': '49%',
		'display': 'flex',
		'margin': 'auto',
		'padding-bottom': '10px',
		'line-height': '1.35',
		'color': 'black',
		'transition': 'box-shadow .3s ease-in-out',
		'border-radius': '2px',
		'box-shadow': '0 2px 4px 0 rgba(0, 0, 0, .32)'
	});
	$(".locationLabel").css({
		'font-weight': 'bold',
		'font-size': 'x-large',
		'padding-top': '10px',
		'padding-left': '5px'
	});
	$(".locationInfo").css({
		'padding-left': '20px',
		'padding-top': '5px',
		'width': '50%'
	});
}