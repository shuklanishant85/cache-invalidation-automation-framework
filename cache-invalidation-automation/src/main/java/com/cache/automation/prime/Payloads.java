package com.cache.automation.prime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Payloads {

	private static final Log LOGGER = LogFactory.getLog(Payloads.class);

	@SuppressWarnings("deprecation")
	public static List<String> getRequestPayloads(String payloadFile) {
		List<String> payloads = null;
		try {
			payloads = FileUtils.readLines(new File(payloadFile));
		} catch (IOException e) {
			LOGGER.error("couldnot read payload file : " + payloadFile);
		}
		if (payloads != null) {
			return payloads;
		}
		return new ArrayList<>();
	}

}
