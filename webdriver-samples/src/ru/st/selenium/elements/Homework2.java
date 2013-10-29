package ru.st.selenium.elements;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;




public class Homework2 {
    FirefoxDriver wd;
   
    
    
    @BeforeMethod
    public void setUp() throws Exception {
    	DesiredCapabilities caps = DesiredCapabilities.firefox();
    	caps.setCapability("nativeEvents", false);
        wd = new FirefoxDriver(caps);
       
    }
   
   
    
    @Test
    
    public void homework2() {
    	
       	String url = "http://demos.telerik.com/aspnet-ajax/webmail/default.aspx";
		wd.get(url);
		PageFactory.initElements(wd, this);
       
		List <WebElement> folders = wd.findElements(By.xpath("/html/body/form/div[4]/div/table/tbody/tr[2]/td/div/div/div/table/tbody/tr/td/div/div/div/table/tbody/tr/td/div/div[3]/ul/li/ul/li[3]/ul/li/div/span[2]"));
        for (WebElement folder : folders){
        	new WebDriverWait (wd,30).until(ExpectedConditions.presenceOfElementLocated(By.id("subject")));
        	folder.click();
        	new WebDriverWait (wd,30).until(ExpectedConditions.stalenessOf(wd.findElement(By.id("ctl00_ContentPlaceHolder2_RadGrid1_ctl00"))));
        	new WebDriverWait (wd,30).until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_ContentPlaceHolder2_RadGrid1_ctl00")));
        	
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


