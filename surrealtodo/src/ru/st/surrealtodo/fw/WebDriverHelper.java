package ru.st.surrealtodo.fw;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.st.selenium.TracingWebDriver;

import com.opera.core.systems.OperaDriver;

public class WebDriverHelper extends HelperBase {
	
	private static Logger log = LoggerFactory.getLogger(WebDriverHelper.class);
	
	private static WebDriver driver;

	public WebDriverHelper(Application manager) {
		super(manager);
		String browser = manager.getProperty("browser");
		log.debug("Going to start {}", browser);
		if (browser == null) {
			log.debug("No browser specified, falling back to firefox");
			browser = "firefox";
		}
		if ("firefox".equals(browser)) {
			FirefoxProfile profile = new FirefoxProfile();
			String firebug = manager.getProperty("firebug");
			if (firebug != null) {
				log.debug("Let's try to load firebug from {}", firebug);
				try {
					profile.addExtension(new File(firebug));
					profile.setPreference("extensions.firebug.currentVersion", "9.9.9");
				} catch (IOException e) {
					log.debug("Firebug can't be loaded, skip it", e);
				}
			}
			driver = new FirefoxDriver(profile);
		} else if ("ie".equals(browser)) {
			driver = new InternetExplorerDriver();
		} else if ("chrome".equals(browser)) {
			driver = new ChromeDriver();
		} else if ("opera".equals(browser)) {
			driver = new OperaDriver();
		} else {
			log.debug("Unknown browser, falling back to firefox");
			driver = new FirefoxDriver();
		}
		driver = new TracingWebDriver(driver);
		log.debug("Browser started");
		log.trace("Open main page {}", manager.getProperty("baseUrl"));
		driver.get(manager.getProperty("baseUrl"));
	}

	public void stop() {
		log.debug("Going to stop browser");
		driver.quit();
		log.debug("Browser stopped");
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setImplicitWaitsOff() {
		log.trace("Set implicit waits off");
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}

	public void setImplicitWaitsOn() {
		log.trace("Set implicit waits on");
		driver.manage().timeouts().implicitlyWait(manager.getTimeout(), TimeUnit.SECONDS);
	}

}
