package com.cache.automation.reporting;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cache.automation.constant.Constants;
import com.cache.automation.listner.FolderListner;
import com.cache.automation.prime.CachePrimer;

public class LogFolderListner extends FolderListner implements Runnable {

	private static final Log LOGGER = LogFactory.getLog(LogFolderListner.class);
	LogsFormatter logsFormatter = new LogsFormatter();

	@Override
	public void run() {
		this.listen(Constants.AUTOMATION_LOG_DIR, StandardWatchEventKinds.ENTRY_CREATE);
	}

	@Override
	public void postListenCallback(WatchEvent<?> event) {
		try {
			LOGGER.info("sleeping execution of report genrator for log-generator to complete task");
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			LOGGER.error("logFolderListener interrupted while sleeping");
			Thread.currentThread().interrupt();
		}
		String contentType = StringUtils.substringBetween(event.context().toString(),
				Constants.RUNTIME + Constants.HYPHEN, Constants.LOG_EXT);
		String filePath = Constants.AUTOMATION_LOG_DIR + event.context().toString();
		LOGGER.info("new file [" + filePath + "] found in folder " + Constants.AUTOMATION_LOG_DIR);
		logsFormatter.createLogReport(filePath, contentType);
		if (Constants.IS_PRIME_ENABLED) {
			CachePrimer primer = new CachePrimer();
			primer.start();
		}
		
	}

	@Override
	public void preListenInit() {
		// do nothing
	}

}
