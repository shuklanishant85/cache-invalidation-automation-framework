package com.cache.automation.invalidation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cache.automation.constant.Constants;

public class DirectoryUtils {

	private static final Log LOGGER = LogFactory.getLog(DirectoryUtils.class);

	private DirectoryUtils() {
		// do nothing
	}

	/**
	 * @return
	 */
	public static List<String> getDeploymentList() {
		List<String> fileList = new ArrayList<>();
		File templateDataFolder = new File(Constants.TEMPLATE_DATA_FOLDER_PATH);
		createContentTypePaths(fileList, templateDataFolder);
		addConfigurationPaths(fileList);
		return fileList;
	}

	/**
	 * @param fileList
	 */
	private static void addConfigurationPaths(List<String> fileList) {
		try {
			List<String> configFiles = Files.readAllLines(Paths.get(Constants.CONFIG_FILES_LIST));
			List<String> fullPathList = new ArrayList<>();
			for (String path : configFiles) {
				System.out.println(Constants.ROOT + path);
				fullPathList.add(Constants.ROOT + path);
			}
			fileList.addAll(fullPathList);
		} catch (IOException e) {
			LOGGER.error("unable to read file: " + Constants.CONFIG_FILES_LIST);
		}
	}

	/**
	 * @param fileList
	 * @param fileDir
	 */
	private static void createContentTypePaths(List<String> fileList, File fileDir) {
		if (fileDir.isDirectory()) {
			if (!StringUtils.equals(fileDir.getName(), Constants.DATA)) {
				List<File> innerDir = Arrays.asList(fileDir.listFiles());
				for (File file : innerDir) {
					createContentTypePaths(fileList, file);
				}
			} else {
				processDataFolder(fileList, fileDir);
			}
		}
	}

	/**
	 * @param fileList
	 * @param fileDir
	 */
	private static void processDataFolder(List<String> fileList, File fileDir) {
		List<File> innerDir = Arrays.asList(fileDir.listFiles());
		for (File file : innerDir) {
			if (!file.isDirectory()) {
				fileList.add(file.getAbsolutePath());
				System.out.println(file.getAbsolutePath());
				if (new File(fileDir.getAbsolutePath() + Constants.DELEMITER + Constants.ENGLISH_LOCALE).exists()) {
					fileList.add(fileDir.getAbsolutePath() + Constants.DELEMITER + Constants.ENGLISH_LOCALE
							+ Constants.DELEMITER + file.getName());
					System.out.println(fileDir.getAbsolutePath() + Constants.DELEMITER + Constants.ENGLISH_LOCALE
							+ Constants.DELEMITER + file.getName());
				}
				break;
			}
		}
	}

	public static String extractContentType(String pathString) {
		String contentType;
		contentType = StringUtils.substringBetween(pathString, Constants.TEMPLATE_DATA, Constants.DATA);
		if (StringUtils.isBlank(contentType)) {
			contentType = StringUtils.substringAfter(pathString,Constants.WEB);
		}
		contentType = StringUtils.replace(contentType, Constants.DELEMITER, Constants.HYPHEN);
		contentType = StringUtils.replace(contentType, Constants.BACKSLASH, Constants.HYPHEN);
		return contentType;
	}

}
