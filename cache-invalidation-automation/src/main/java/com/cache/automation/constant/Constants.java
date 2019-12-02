package com.cache.automation.constant;

import org.apache.commons.lang3.StringUtils;

public class Constants {

	private Constants() {
		// do nothing
	}

	public static final String DATA = "data";
	public static final String ENGLISH_LOCALE = "en_US";
	public static final String DELEMITER = "/";
	public static final String TEMP = "temp";
	public static final String HYPHEN = "-";
	public static final String TEXT_EXT = ".txt";
	public static final String TEMPLATE_DATA = "templatedata";
	public static final String BACKSLASH = "\\";
	public static final String LOG_EXT = ".log";
	public static final String RUNTIME = "runtime";
	public static final String PIPE = "|";
	public static final String LOG_LEVEL_REGEX = "[ \\t][A-Z]{4,5}[ \\t]";
	public static final String CLASS_NAME_REGEX = "[ \\t][\\[][\\w .]+[\\]]";
	public static final String TNX_ID_REGEX = "[ \\t][\\[][\\w -]*[\\]]";
	public static final String LOG_STATEMENT_REGEX = "(?<=([ \\t][\\[][\\w .]{1,100}[\\]])).*$";
	public static final String LOCALIZED = "-localized";
	public static final String WEB = "web";
	public static final String DOT = ".";

	public static final String INVOKE_INVAL_LOG = AutomationProperties.getProperty("inval.start.string");
	public static final String SPA_REQUEST_URI = AutomationProperties.getProperty("spa.request.uri");
	public static final String CCLAMP_REQUEST_URI = AutomationProperties.getProperty("cclamp.requst.uri");
	public static final String SPA_PAYLOADS = AutomationProperties.getProperty("spa.payloads.file");
	public static final String CCLAMP_PAYLOADS = AutomationProperties.getProperty("cclamp.payloads.file");
	public static final String BROWSER_URLS = AutomationProperties.getProperty("browser.url.file");
	public static final String RUNTIME_BACKUP_PATH = AutomationProperties.getProperty("runtime.backup.path");
	public static final String CONFIG_FILES_LIST = AutomationProperties.getProperty("config.files.list.path");
	public static final String ROOT = AutomationProperties.getProperty("web.root.path");
	public static final String LOG_REPORT = AutomationProperties.getProperty("log.report.sheets");
	public static final String REGEX_FILE = AutomationProperties.getProperty("regex.file.path");
	public static final String AUTOMATION_LOG_DIR = AutomationProperties.getProperty("automation.log.path");
	public static final String TEMP_DIR_PATH = AutomationProperties.getProperty("temp.directory.path");
	public static final long INVALIDATION_MONITOR_WAIT_TIME = Long
			.parseLong(AutomationProperties.getProperty("invalidation.monitor.wait.time"));
	public static final long PERIOD = StringUtils
			.isNotBlank(AutomationProperties.getProperty("invalidation.init.wait.time"))
					? Long.parseLong(AutomationProperties.getProperty("invalidation.init.wait.time"))
					: (INVALIDATION_MONITOR_WAIT_TIME + 60000);
	public static final String RUNTIME_LOG_PATH = AutomationProperties.getProperty("runtime.log.path");
	public static final long DELAY = Long.parseLong(AutomationProperties.getProperty("invalidation.delay.wait.time"));
	public static final String LOGGER_PROPERTIES_PATH = AutomationProperties.getProperty("log4j.properties.path");
	public static final String TEMPLATE_DATA_FOLDER_PATH = AutomationProperties.getProperty("template.data.path");
	public static final long DELAY_BETWEEN_PRIMES = Long
			.parseLong(AutomationProperties.getProperty("delay.between.primes"));
	public static final boolean IS_PRIME_ENABLED = Boolean
			.parseBoolean(AutomationProperties.getProperty("is.prime.enbled"));
	public static final String EXECUTE_COPY_COMMAND = AutomationProperties.getProperty("log.copy.command");
	public static final boolean IS_LINUX_SERVER = Boolean.parseBoolean(AutomationProperties.getProperty("is.linux.server"));
	public static final String EXECUTE_SCRIPT_FILE = AutomationProperties.getProperty("awk.script.file.command");

}
