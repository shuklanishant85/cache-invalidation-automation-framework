package com.cache.connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class ConnectionManager {
	private static final Log LOGGER = LogFactory.getLog(ConnectionManager.class);

	/**
	 * @return
	 */
	public PoolingHttpClientConnectionManager getConnectionManager() {
		LOGGER.info("creating new pooling connection manager");
		PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
		poolingConnectionManager.setMaxTotal(1);
		poolingConnectionManager.setDefaultMaxPerRoute(1);
		return poolingConnectionManager;
	}
}
