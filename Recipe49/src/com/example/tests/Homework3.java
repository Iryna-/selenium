package com.example.tests;


import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import ru.st.surrealtodo.tests.TestBase;
import ru.st.testng.TracingListener;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

		
	public class Homework3 {
	
		private static Logger log = LoggerFactory.getLogger(Homework3.class);
			static{
				Logger.getLogger("").setLevel(Level.ALL);
				for(Handler h:Logger.getLogger("").getHandlers()){
				Logger.getLogger("").removeHandler(h);
				}
				SLF4J.BridgeHandler.install();
				SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
				StatusPrinter.print((LoggerContext)LoggerFactory.getILoggerFactory());
			}
			
			
		    FirefoxDriver wd;
		    
		    @BeforeMethod
		    public void setUp() throws Exception {
		        wd = new FirefoxDriver();
		        wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		       // wd.setLogLevel(Level.INFO);
		    }
		    
		    @Test
		    public void hw3() {
		        String url = "http://localhost/php4dvd/";
				wd.get(url);
		        wd.findElement(By.id("wrapper")).click();
		        wd.findElement(By.id("username")).click();
		        wd.findElement(By.id("username")).clear();
		        wd.findElement(By.id("username")).sendKeys("admin");
		        wd.findElement(By.name("password")).click();
		        wd.findElement(By.name("password")).clear();
		        wd.findElement(By.name("password")).sendKeys("admin");
		        wd.findElement(By.name("submit")).click();
		        
		        
		        List <WebElement> movies = wd.findElements(By.className("title"));

		        
		        for (WebElement movie : movies){
			        String movie_name=movie.getText();
			        System.out.println("Movie before search: " + movie_name);
			        }
		       
		        wd.findElement(By.id("q")).clear();
		       
		        wd.findElement(By.id("q")).sendKeys(Keys.ENTER);
		        
		        String searchTerm = "love";
				wd.findElement(By.id("q")).sendKeys(searchTerm+Keys.ENTER);
		        
				
		        new WebDriverWait (wd,30).until(ExpectedConditions.stalenessOf(movies.get(0)));
		       	       
		           	List <WebElement> movies_filtered = wd.findElements(By.className("title"));
			       
		           	for (WebElement movie_filtered : movies_filtered){
			        String movie_name=movie_filtered.getText();
			        System.out.println("Movie after search: " + movie_name);
			        Assert.assertThat(movie_name.toLowerCase(), Matchers.containsString(searchTerm.toLowerCase()));
			        
		        
		       }
		    }
		    
		    @AfterMethod
		    public void tearDown() {
		        wd.close();
		    }
		    
		    public static boolean isAlertPresent(FirefoxDriver wd) {
		        try {
		            wd.switchTo().alert();
		            return true;
		        } catch (NoAlertPresentException e) {
		            return false;
		        }
		    }
		}


}
