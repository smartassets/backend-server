httpClient = {
	get: function( url, data, callback ) {
		var xhr = new XMLHttpRequest();
		
		xhr.onreadystatechange = function () {
			var readyState = xhr.readyState;

			if (readyState == 4) {
				callback(xhr);
			}
		};
		
		var queryString = '';
		if (typeof data === 'object') {
			for (var propertyName in data) {
				queryString += (queryString.length === 0 ? '' : '&') + propertyName + '=' + encodeURIComponent(data[propertyName]);
			}
		}
		
		if (queryString.length !== 0) {
			url += (url.indexOf('?') === -1 ? '?' : '&') + queryString;
		}
		
		xhr.open('GET', url, true);
		xhr.send(null);
	},
	
	post: function( url, data, callback ) {
		var xhr = new XMLHttpRequest();
		
		xhr.onreadystatechange = function () {
			var readyState = xhr.readyState;

			if (readyState == 4) {
				callback(xhr);
			}
		};
		
		var queryString = '';
		if (typeof data === 'object') {
			for (var propertyName in data) {
				queryString += (queryString.length === 0 ? '' : '&') + propertyName + '=' + encodeURIComponent(data[propertyName]);
			}
		}
		
		xhr.open('POST', url, true);
		xhr.setRequestHeader('content-type', 'application/x-www-form-urlencoded');
		xhr.send(queryString);
	},

	createCORSRequest: function(method, url) {
		var xhr = new XMLHttpRequest();
		if ("withCredentials" in xhr) {
	  
		  // Check if the XMLHttpRequest object has a "withCredentials" property.
		  // "withCredentials" only exists on XMLHTTPRequest2 objects.
		  xhr.open(method, url, true);
	  
		} else if (typeof XDomainRequest != "undefined") {
	  
		  // Otherwise, check if XDomainRequest.
		  // XDomainRequest only exists in IE, and is IE's way of making CORS requests.
		  xhr = new XDomainRequest();
		  xhr.open(method, url);
	  
		} else {
	  
		  // Otherwise, CORS is not supported by the browser.
		  xhr = null;
	  
		}
		return xhr;
	  },

	delete: function( url, data, callback ) {
		var xhr = new XMLHttpRequest();
		
		xhr.onreadystatechange = function () {
			var readyState = xhr.readyState;

			if (readyState == 4) {
				callback(xhr);
			}
		};
		
		var queryString = '';
		if (typeof data === 'object') {
			for (var propertyName in data) {
				queryString += (queryString.length === 0 ? '' : '&') + propertyName + '=' + encodeURIComponent(data[propertyName]);
			}
		}
		
		if (queryString.length !== 0) {
			url += (url.indexOf('?') === -1 ? '?' : '&') + queryString;
		}
		
		xhr.open('DELETE', url, true);
		xhr.send(null);
	}
};