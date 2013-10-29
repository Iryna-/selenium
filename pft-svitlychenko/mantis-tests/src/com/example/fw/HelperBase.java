package com.example.fw;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperBase {
	
	public ApplicationManager manager;
	public HelperBase (ApplicationManager manager) {
		this.manager = manager;
		
	}

	protected void pause(int pause) {
		try {
			Thread.sleep(pause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
