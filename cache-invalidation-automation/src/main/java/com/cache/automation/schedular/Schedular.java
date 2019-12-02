package com.cache.automation.schedular;

import java.util.Timer;

import com.cache.automation.constant.Constants;

public class Schedular {


	public void init() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new Invalidator(), Constants.DELAY, Constants.PERIOD);
	}
}
