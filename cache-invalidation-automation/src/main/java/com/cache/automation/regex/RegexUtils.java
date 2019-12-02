package com.cache.automation.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.cache.automation.constant.Constants;

public class RegexUtils {

	public static final Pattern LOG_LEVEL;
	public static final Pattern TNX_ID;
	public static final Pattern CLASS_NAME;
	public static final Pattern LOG_STATEMENT;

	static {
		LOG_LEVEL = Pattern.compile(Constants.LOG_LEVEL_REGEX);
		TNX_ID = Pattern.compile(Constants.TNX_ID_REGEX);
		CLASS_NAME = Pattern.compile(Constants.CLASS_NAME_REGEX);
		;
		LOG_STATEMENT = Pattern.compile(Constants.LOG_STATEMENT_REGEX);
		;
	}

	private RegexUtils() {
		// do nothing
	}

	public static String match(Pattern pattern, String statement) {
		Matcher matcher = pattern.matcher(statement);
		if (matcher.find()) {
			return matcher.group(0);
		}
		return StringUtils.EMPTY;
	}

}
