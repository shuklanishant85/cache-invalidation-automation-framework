package com.cache.automation.constant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AutomationProperties {

	private static final Log LOGGER = LogFactory.getLog(AutomationProperties.class);
	private static String propertyFilePath;
	private static AutomationProperties instance = null;
	private static Properties properties;

	private AutomationProperties() {
		// do nothing
	}

	public static void setPropertyFilePath(String path) {
		propertyFilePath = path;
	}
	private static synchronized AutomationProperties getInstance() {
		if (null == instance) {
			synchronized (AutomationProperties.class) {
				if (null == instance) {
					instance = new AutomationProperties();
					properties = new Properties();
					try {
						InputStream stream = FileUtils.openInputStream(new File(propertyFilePath));
						properties.load(stream);
					} catch (IOException e) {
						LOGGER.error("could not read properties file : " + e.getMessage());
					}

				}
			}

		}
		return instance;
	}

	public static String getProperty(String key) {
		if (properties == null) {
			instance = getInstance();
		}

		return properties.getProperty(key);
	}

}
