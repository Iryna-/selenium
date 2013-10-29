package com.example.tests;

import java.io.FileReader;
import java.util.Properties;
import java.io.File;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import com.example.fw.ApplicationManager;



public class TestBase {

	protected static ApplicationManager app;
	
	
	@BeforeTest
	public void setUp() throws Exception {
		String configFile = System.getProperty("configFile", "application.properties");
		Properties properties = new Properties();
		properties.load(new FileReader(new File(configFile)));
		app = ApplicationManager.getIntance(properties);
		
	}

	
	
	
	@AfterTest
	public void tearDown() throws Exception {
		ApplicationManager.getIntance(null).stop();
		
	}
	
	
}
