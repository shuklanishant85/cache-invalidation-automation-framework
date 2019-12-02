package com.cache.automation.listner;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cache.automation.constant.Constants;
import com.cache.automation.invalidation.LogFileUtils;

public abstract class FolderListner {
	
	private static final Log LOGGER = LogFactory.getLog(FolderListner.class);

	public void listen(String folderPath, Kind<?> eventKind) {
		Path listenPath = Paths.get(folderPath);
		try {
			preListenInit();
			if (!Constants.IS_LINUX_SERVER) {
				LogFileUtils.backupLogFile(Constants.RUNTIME_LOG_PATH, Constants.RUNTIME_BACKUP_PATH);
			}
			WatchService listner = FileSystems.getDefault().newWatchService();
			listenPath.register(listner, eventKind);
			boolean listening = true;
			do {
				WatchKey watchKey = listner.take();
				for (WatchEvent<?> event : watchKey.pollEvents()) {
					if (eventKind.equals(event.kind())) {
						postListenCallback(event);
					}
				}
				listening = watchKey.reset();
			} while (listening);

		} catch (IOException e) {
			LOGGER.error("exception occured while creating listner for path: " + folderPath);
		} catch (InterruptedException e) {
			LOGGER.error("interruption exception occured while listening: " + folderPath);
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * 
	 */
	public abstract void postListenCallback(WatchEvent<?> event);

	/**
	 * 
	 */
	public abstract void preListenInit();
}
