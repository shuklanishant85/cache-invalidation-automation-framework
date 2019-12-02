package com.cache.automation.schedular;

import java.util.TimerTask;

import com.cache.automation.invalidation.CacheFileCreator;

public class Invalidator extends TimerTask {

	@Override
	public void run() {
		initInvalidation();
	}
	
	public void initInvalidation() {
		CacheFileCreator.createTempFile();
	}
	
}
