package com.cache.automation.reporting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cache.automation.constant.Constants;
import com.cache.automation.regex.RegexUtils;

/**
 * @author nishant shukla
 *
 */
public class LogsFormatter {

	private static final Log LOGGER = LogFactory.getLog(LogsFormatter.class);
	Workbook workbook = new XSSFWorkbook();

	/**
	 * @param filePath
	 * @param contentType
	 */
	public void createLogReport(String filePath, String contentType) {
		try (FileOutputStream out = new FileOutputStream(Constants.LOG_REPORT)) {
			Sheet contentTypeSheet = workbook.createSheet(contentType);
			writeData(contentTypeSheet, filePath);
			workbook.write(out);

		} catch (FileNotFoundException e) {
			LOGGER.error("cannot find workbook :  " + Constants.LOG_REPORT + "\n" + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("cannot read workbook : " + Constants.LOG_REPORT);
		} catch (Exception e) {
			LOGGER.error("cannot create workbook : " + Constants.LOG_REPORT);
		}
	}

	/**
	 * @param contentTypeSheet
	 * @param logPath
	 */
	private static void writeData(Sheet contentTypeSheet, String logPath) {
		if (StringUtils.isNoneBlank(logPath)) {
			try {
				int rowCount = 0;
				@SuppressWarnings("deprecation")
				List<String> log = FileUtils.readLines(new File(logPath));
				String validTransactionId = StringUtils.EMPTY;
				boolean findInvalId = true;
				if (!log.isEmpty()) {
					for (String logString : log) {
						String logTnxId = RegexUtils.match(RegexUtils.TNX_ID, logString);
						if (StringUtils.isNotBlank(logTnxId.replace("[", "").replace("]", ""))) {
							String logMessage = RegexUtils.match(RegexUtils.LOG_STATEMENT, logString);
							if (findInvalId && StringUtils.equals(logMessage.trim(), Constants.INVOKE_INVAL_LOG)) {
								validTransactionId = RegexUtils.match(RegexUtils.TNX_ID, logString);
								findInvalId = false;
							}
							rowCount = fillRowWithData(contentTypeSheet, rowCount, logMessage, logTnxId, logString,
									validTransactionId);
						}
					}
				}
			} catch (IOException e) {
				LOGGER.error("cannot read file: " + logPath);
			}
		}
	}

	/**
	 * @param colCount
	 * @param row
	 * @param logMessage
	 * @param logTnxId
	 * @param logLevel
	 * @param className
	 * @param validTransactionId
	 */
	public static int fillRowWithData(Sheet contentTypeSheet, int rowCount, String logMessage, String logTnxId,
			String logString, String validTransactionId) {
		int colCount = 0;
		if (StringUtils.equals(logTnxId, validTransactionId)) {
			String logLevel = RegexUtils.match(RegexUtils.LOG_LEVEL, logString);
			String className = RegexUtils.match(RegexUtils.CLASS_NAME, logString);
			Row row = contentTypeSheet.createRow(rowCount++);
			row.createCell(colCount + 1).setCellValue(StringUtils.isNotBlank(logLevel) ? logLevel : StringUtils.EMPTY);
			row.createCell(colCount + 2).setCellValue(StringUtils.isNotBlank(logTnxId) ? logTnxId : StringUtils.EMPTY);
			row.createCell(colCount + 3)
			.setCellValue(StringUtils.isNotBlank(className) ? className : StringUtils.EMPTY);
			row.createCell(colCount + 4)
			.setCellValue(StringUtils.isNotBlank(logMessage) ? logMessage : StringUtils.EMPTY);
		}
		return rowCount;
	}

	public void destroyThread() {
		try {
			workbook.close();
		} catch (IOException e) {
			LOGGER.error("unable to close workbook : " + e.getMessage());
		}
	}

}
