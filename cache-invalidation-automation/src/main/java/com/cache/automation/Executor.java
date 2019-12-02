package com.cache.automation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.cache.automation.constant.AutomationProperties;
import com.cache.automation.constant.Constants;
import com.cache.automation.invalidation.InvalidationListner;
import com.cache.automation.prime.CachePrimer;
import com.cache.automation.reporting.LogFolderListner;
import com.cache.automation.schedular.Schedular;

public class Executor {
	public static void main(String[] args) {
		final Log appLogger = LogFactory.getLog(Executor.class);

		if (args.length != 0) {
			String propertyFilePath = args[0];
			if (StringUtils.isNoneBlank(propertyFilePath)) {
				AutomationProperties.setPropertyFilePath(propertyFilePath);
			}else {
				appLogger.error("please provide property file path");
			}
		}

		PropertyConfigurator.configure(Constants.LOGGER_PROPERTIES_PATH);

		// start listening on log folder for creating excel reports
		Thread logFolderListnerThread = new Thread(new LogFolderListner());
		logFolderListnerThread.start();

		// start listening to temp folder for deleted temp-file 
		Thread invalidationListnerThread = new Thread(new InvalidationListner(Constants.TEMP_DIR_PATH));
		invalidationListnerThread.start();
		if (Constants.IS_PRIME_ENABLED) {
			CachePrimer primer = new CachePrimer();
			primer.start();
		}
	
		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			appLogger.error("application main thread interrupted while executing");
			Thread.currentThread().interrupt();
		}

		new Schedular().init();
	}

}
