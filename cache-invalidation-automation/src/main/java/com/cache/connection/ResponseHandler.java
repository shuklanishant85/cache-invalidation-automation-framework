package com.cache.connection;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

public class ResponseHandler {

	private static final Log LOGGER = LogFactory.getLog(ResponseHandler.class);

	/**
	 * @param response
	 * @throws IOException
	 */
	public void processResponse(HttpResponse response) throws IOException {
		Header[] headers = response.getAllHeaders();
		for (Header header : headers) {
			LOGGER.info("Response Header Key : " + header.getName() + " ,Value : " + header.getValue());
		}

		StatusLine status = response.getStatusLine();
		if (status.getStatusCode() == 200) {
			LOGGER.info("valid response received : cache created for service request");
			LOGGER.info("response status : " + status.getStatusCode());
		}
		EntityUtils.consume(response.getEntity());
	}
}
