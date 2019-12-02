package com.cache.automation.invalidation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cache.automation.constant.Constants;

public class CacheFileCreator {

	private static final Log LOGGER = LogFactory.getLog(CacheFileCreator.class);
	static List<String> deploymentList;

	private CacheFileCreator() {
		// do nothing
	}

	/**
	 * 
	 */
	public static void createTempFile() {
		if (null == deploymentList) {
			deploymentList = DirectoryUtils.getDeploymentList();
		}
		if (!deploymentList.isEmpty()) {
			String contentType = DirectoryUtils.extractContentType(deploymentList.get(0));
			if (StringUtils.endsWith(contentType, Constants.HYPHEN)) {
				contentType = StringUtils.removeEnd(contentType, Constants.HYPHEN);
			}
			contentType = StringUtils.replace(contentType, Constants.DOT, Constants.HYPHEN);
			String tempFileName = StringUtils.EMPTY;
			if (StringUtils.containsIgnoreCase(deploymentList.get(0), Constants.ENGLISH_LOCALE)) {
				tempFileName = Constants.LOCALIZED + tempFileName;
			}
			tempFileName = Constants.TEMP + tempFileName + contentType + Constants.TEXT_EXT;
			File tempFile = new File(Constants.TEMP_DIR_PATH + tempFileName);

			try {
				boolean flag = tempFile.createNewFile() && tempFile.setExecutable(true) && tempFile.setReadable(true)
						&& tempFile.setWritable(true);
				if (flag) {
					if (Constants.IS_LINUX_SERVER) {
						setPermissions(Constants.TEMP_DIR_PATH + tempFileName);
					}
					writeDeploymentPathToTempFile(tempFileName, tempFile);
					new Thread(() -> {
							try {
								Thread.sleep(Constants.INVALIDATION_MONITOR_WAIT_TIME - 5);
								if (!Constants.IS_LINUX_SERVER) {
									LogFileUtils.clearFileContent(Constants.RUNTIME_LOG_PATH);
								}	
							} catch (InterruptedException e) {
								LOGGER.error("thread interrupted while cleaning log file");
								Thread.currentThread().interrupt();
							} catch (IOException e) {
								LOGGER.error("File not found : " + Constants.RUNTIME_LOG_PATH);
							}
					});
				} else {
					LOGGER.error("unable to create temp file: " + tempFile.getPath());
				}

			} catch (IOException e) {
				LOGGER.error("Error while Creating File in temp folder : " + e);
			}
			LOGGER.info("temp file: " + tempFile.getPath() + " created ");
		} else {
			Thread.currentThread().interrupt();
		}

	}

	private static void setPermissions(String path) {
		 Set<PosixFilePermission> perms;
		try {
			perms = Files.readAttributes(Paths.get(path),PosixFileAttributes.class).permissions();

	        System.out.format("Permissions before: %s%n",  PosixFilePermissions.toString(perms));

	        perms.add(PosixFilePermission.OWNER_WRITE);
	        perms.add(PosixFilePermission.OWNER_READ);
	        perms.add(PosixFilePermission.OWNER_EXECUTE);
	        perms.add(PosixFilePermission.GROUP_WRITE);
	        perms.add(PosixFilePermission.GROUP_READ);
	        perms.add(PosixFilePermission.GROUP_EXECUTE);
	        perms.add(PosixFilePermission.OTHERS_WRITE);
	        perms.add(PosixFilePermission.OTHERS_READ);
	        perms.add(PosixFilePermission.OTHERS_EXECUTE);
			Files.setPosixFilePermissions(Paths.get(path), perms);
			} catch (IOException e) {
				LOGGER.error("failed to update permissions for file : " + path);
			}		
	}

	/**
	 * @param tempFileName
	 * @param tempFile
	 */
	private static void writeDeploymentPathToTempFile(String tempFileName, File tempFile) {
		Path path = Paths.get(Constants.TEMP_DIR_PATH + tempFileName);
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writer.write(StringUtils.replaceChars(deploymentList.get(0), "\\", "/"));
			LOGGER.info("added path into temp file : [" + deploymentList.get(0) + "]");
			deploymentList.remove(0);
		} catch (IOException ex) {
			LOGGER.error("unable to write into temp file : " + tempFile.getPath());
		}
	}

}
