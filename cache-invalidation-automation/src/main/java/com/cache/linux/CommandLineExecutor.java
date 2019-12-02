package com.cache.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cache.automation.constant.Constants;

public class CommandLineExecutor {

	private static final Log LOGGER = LogFactory.getLog(CommandLineExecutor.class);

	public void startLogParsing(String tempFileName, String logFileName) {
		replaceTokensInScript("{0}", tempFileName, "{1}", logFileName);
		Runtime runtime = Runtime.getRuntime();
		executeCommand(tempFileName, runtime);
		replaceTokensInScript(tempFileName, "{0}", logFileName, "{1}");
	}

	public void executeCommand(String command, Runtime runtime) {
		try {
			LOGGER.info("starting command execution on Server");
			Process process = runtime.exec(Constants.EXECUTE_SCRIPT_FILE);
			process.waitFor();
			LOGGER.info("executed script file : " + command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String consoleOutput;
			while (StringUtils.isNoneBlank((consoleOutput = reader.readLine()))) {
				LOGGER.info("console >> " + consoleOutput);
			}
		} catch (IOException e) {
			LOGGER.error("exception occured while executing command : " + command + "\n " + e);
		} catch (InterruptedException e) {
			LOGGER.error("process interrupted while executing command : " + command + "\n " + e);
			Thread.currentThread().interrupt();
		}
	}

	private void replaceTokensInScript(String firstToken, String firstReplacement, String secondToken,
			String secondReplacement) {
		Path path = Paths.get(Constants.EXECUTE_SCRIPT_FILE);
		try {
			List<String> commands = new ArrayList<>();
			List<String> rawCommands = Files.readAllLines(path);
			for (String command : rawCommands) {
				command = StringUtils.replace(command, firstToken, firstReplacement);
				command = StringUtils.replace(command, secondToken, secondReplacement);
				if (StringUtils.isNotBlank(command)) {
					commands.add(command);
				}
			}
			Files.write(path, commands, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			LOGGER.error("cannot read file : " + Constants.EXECUTE_SCRIPT_FILE);
		}

	}
	
}
