package com.example.tests;

import java.io.FileReader;
import java.util.Properties;
import java.io.File;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import com.example.fw.ApplicationManager;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;


public class TestBase {

	protected static ApplicationManager app;
	
	
	@BeforeClass
	@Parameters({"configFile"})
	public void setUp(@Optional String configFile) throws Exception {
		if (configFile == null) {
			configFile = System.getProperty("configFile");
		}
		if (configFile == null) {
			configFile = System.getenv("configFile");
		}
		if (configFile == null) {
			configFile = "application.properties";
		}
		Properties props = new Properties();
		props.load(new FileReader(configFile));
		app = ApplicationManager.getInstance();
		app.setProperties(props);
		
	}
	
		
	@AfterTest
	public void tearDown() throws Exception {
		app.stop();
		
	}
	
	
}




