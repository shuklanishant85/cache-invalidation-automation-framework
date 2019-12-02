package com.cache.connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author nisshukl0
 *
 */
public class ConnectionPool {

	private static final Log LOGGER = LogFactory.getLog(ConnectionPool.class);

	CloseableHttpClient client;
	HttpPost post;

	/**
	 * 
	 */
	public ConnectionPool() {
		ConnectionManager manager = new ConnectionManager();
		client = HttpClients.custom().setConnectionManager(manager.getConnectionManager()).build();
	}

	/**
	 * @param uri
	 */
	public void executeRequest(String uri, String payload) {
		LOGGER.info("starting service request for service : " + uri);
		post = new HttpPost(uri);
		HttpClientConnectionExecutor executor = new HttpClientConnectionExecutor(post, client, payload);
		executor.start();
	}

}
