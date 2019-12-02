package com.cache.automation.prime;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cache.automation.constant.Constants;
import com.cache.automation.invalidation.LogFileUtils;
import com.cache.connection.ConnectionPool;

public class CachePrimer extends Thread {

	private static final Log LOGGER = LogFactory.getLog(CachePrimer.class);
	static List<String> cclampPayloads;
	static List<String> spaPayloads;
	static List<String> browserUrls;

	static {
		spaPayloads = Payloads.getRequestPayloads(Constants.SPA_PAYLOADS);
		cclampPayloads = Payloads.getRequestPayloads(Constants.CCLAMP_PAYLOADS);
		browserUrls = Payloads.getRequestPayloads(Constants.BROWSER_URLS);
	}

	public void executePriming(RequestType type) {
		if (type == RequestType.BROWSER_REQUEST) {
			for (String url : browserUrls) {
				createPageRequest(url);
			}
		} else if (type == RequestType.CCLAMP_REQUEST) {
			for (String payload : cclampPayloads) {
				new ConnectionPool().executeRequest(Constants.CCLAMP_REQUEST_URI, payload);
			}
		} else if (type == RequestType.SPA_REQUEST) {
			for (String payload : spaPayloads) {
				new ConnectionPool().executeRequest(Constants.SPA_REQUEST_URI, payload);
			}
		}
	}

	public void createPageRequest(String url) {
		try {
			Desktop desktop = java.awt.Desktop.getDesktop();
			URI oURL = new URI(url);
			desktop.browse(oURL);
		} catch (Exception e) {
			LOGGER.error("unable to open page " + url);
		}
	}

	public void executeAllPriming() {
		this.executePriming(RequestType.BROWSER_REQUEST);
		this.executePriming(RequestType.SPA_REQUEST);
		this.executePriming(RequestType.CCLAMP_REQUEST);

	}

	@Override
	public void run() {
		executeAllPriming();
	}

	/**
	 * prime all cache regions before invalidation scenario
	 */
	public static void clearLogsAfterPriming() {
		try {
			Thread.sleep(Constants.DELAY_BETWEEN_PRIMES);
		} catch (InterruptedException e) {
			LOGGER.error("primer interrupted while executing");
			Thread.currentThread().interrupt();
		}
		try {
			if (!Constants.IS_LINUX_SERVER) {
				LogFileUtils.clearFileContent(Constants.RUNTIME_LOG_PATH);
			}
		} catch (IOException e) {
			LOGGER.error("could not clean file content : " + Constants.RUNTIME_LOG_PATH);
		}
	}

}
