package com.cache.connection;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

public class HttpClientConnectionExecutor extends Thread {
	private static final Log LOGGER = LogFactory.getLog(HttpClientConnectionExecutor.class);
	private CloseableHttpClient client;
	private HttpPost post;
	private String payload;

	/**
	 * @param get
	 * @param client
	 */
	public HttpClientConnectionExecutor(HttpPost post, CloseableHttpClient client, String payload) {
		this.post = post;
		this.client = client;
		this.payload = payload;
	}

	@Override
	public void run() {
		executeRequest();
	}

	public void executeRequest() {
		try {
			post.addHeader("content-type", "application/json");
			StringEntity entity = new StringEntity(payload);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			new ResponseHandler().processResponse(response);
		} catch (IOException e) {
			LOGGER.error("exception occured while execution : " + e.getMessage());
		}
		try {
			client.close();
		} catch (IOException e) {
			LOGGER.error("unable to close pool");
		}
	}
}
