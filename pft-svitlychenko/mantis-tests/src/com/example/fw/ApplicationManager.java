package com.example.fw;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;


public class ApplicationManager {

	private static ApplicationManager singleton;
	private WebDriver driver;
	public String baseUrl;
	private HibernateHelper hibernateHelper;
	private Properties properties;
	private AccountHelper accountHelper;
	private MailHelper mailHelper;
	private JamesHelper jamesHelper;

	
		public static ApplicationManager getInstance() {
			if (singleton == null) {
				singleton = new ApplicationManager();
			}
			return singleton;
		}
		
			
	public WebDriver getDriver() {
		String browser = properties.getProperty("browser");
		if (driver == null){
			
			if ("firefox".equals(browser)){
				driver = new FirefoxDriver();
			} 
			else if ("ie".equals(browser)){
				driver = new InternetExplorerDriver();
				baseUrl = properties.getProperty("baseUrl");
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				driver.get(baseUrl);
				
			}
			} 
			else {
				throw new Error ("Unsupported browser: " + browser);
			}
		
		return driver;
	}

	
	
	public void setProperties (Properties properties){
		this.properties = properties;
	}
	
	public String getProperty (String key){
		return properties.getProperty(key);
	}
	
	public HibernateHelper getHibernateHelper() {

		if (hibernateHelper == null){
			
			hibernateHelper = new HibernateHelper (this);
			
		}
		return hibernateHelper;
	
	}

	public AccountHelper getAccountHelper() {
		if (accountHelper == null){
			
			accountHelper = new AccountHelper(this);
			
		}
		return accountHelper;
	
	}
	
	public void stop() {
		driver.quit();
	}



	public MailHelper getMailHelper() {
		if (mailHelper == null){
			
		mailHelper = new MailHelper(this);
			
		}
		return mailHelper;
	
	}



	public JamesHelper getJamesHelper() {
		if (jamesHelper == null){
			
			jamesHelper = new JamesHelper(this);
				
			}
			return jamesHelper;
	}

}
