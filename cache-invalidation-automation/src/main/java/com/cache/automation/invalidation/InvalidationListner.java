package com.cache.automation.invalidation;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cache.automation.constant.Constants;
import com.cache.automation.listner.FolderListner;

public class InvalidationListner extends FolderListner implements Runnable {

	private static final Log LOGGER = LogFactory.getLog(InvalidationListner.class);
	private String listenFolder;

	public InvalidationListner(String listenFolder) {
		this.listenFolder = listenFolder;
	}

	@Override
	public void run() {
		LOGGER.info("starting Folder listner thread for folder : " + listenFolder);
		this.listen(listenFolder, StandardWatchEventKinds.ENTRY_DELETE);
	}

	@Override
	public void postListenCallback(WatchEvent<?> event) {
		String contentType = StringUtils.substringBetween(event.context().toString(), Constants.TEMP + Constants.HYPHEN,
				Constants.TEXT_EXT);
		LOGGER.info("invalidation complete for file [" + event.context().toString() + "] from folder " + listenFolder);
			LogFileUtils.createLogFile(Constants.RUNTIME_LOG_PATH, Constants.AUTOMATION_LOG_DIR, contentType, event.context().toString());	
	}

	@Override
	public void preListenInit() {
		if (!Constants.IS_LINUX_SERVER) {
			LogFileUtils.backupLogFile(Constants.RUNTIME_LOG_PATH, Constants.RUNTIME_BACKUP_PATH);
		}
	}

}
