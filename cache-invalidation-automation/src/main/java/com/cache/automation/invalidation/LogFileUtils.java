package com.cache.automation.invalidation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cache.automation.constant.Constants;
import com.cache.linux.CommandLineExecutor;

public class LogFileUtils {

	private static final Log LOGGER = LogFactory.getLog(LogFileUtils.class);

	private LogFileUtils() {
		// do nothing
	}

	/**
	 * @param logFilePath
	 * @param backup
	 */
	public static void backupLogFile(String logFilePath, String backup) {
		Path path = Paths.get(logFilePath);
		if (path.toFile().exists()) {
			try {
				FileUtils.copyFile(new File(logFilePath), new File(backup));
				clearFileContent(logFilePath);
			} catch (IOException e) {
				LOGGER.error("unable to backup file: " + logFilePath);
			}
		}

	}

	public static void clearFileContent(String logFilePath) throws IOException {
		String cleanData = "-";
		Files.write(Paths.get(logFilePath), cleanData.getBytes());
		LOGGER.info("cleanup successfull");
	}

	/**
	 * @param logFilePath
	 */
	public static void clearLogFile(String logFilePath) {
		Path path = Paths.get(logFilePath);
		try {
			if (Files.deleteIfExists(path)) {
				Files.createFile(path);
			}
		} catch (IOException e) {
			LOGGER.error("unable to create file: " + logFilePath);
		}
	}

	/**
	 * @param logFilePath
	 * @param backup
	 */
	public static void createLogFile(String logFilePath, String automationLogFolder, String contentType, String tempFileName) {
		Path path = Paths.get(logFilePath);
		if (path.toFile().exists()) {
			String extractedLogFolderPath = automationLogFolder + Constants.RUNTIME
					+ Constants.HYPHEN + contentType + Constants.LOG_EXT;
			if (Constants.IS_LINUX_SERVER) {
				new CommandLineExecutor().startLogParsing(tempFileName, extractedLogFolderPath);
			} else {
				try {
					FileUtils.copyFile(new File(logFilePath), new File(extractedLogFolderPath));
					clearFileContent(logFilePath);
				} catch (IOException e) {
					LOGGER.error("unable to backup file : " + logFilePath);
				}
			}
		}
	}
}