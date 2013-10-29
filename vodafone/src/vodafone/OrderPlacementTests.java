package vodafone;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;


public class OrderPlacementTests {
    WebDriver wd;
    String url = "http://vodafone-free-sim-rhombus.testing.agencyrepublic.net/";
    
    @BeforeClass
    public void setUp() throws Exception {
        //wd = new ChromeDriver();
    	
    	
    	DesiredCapabilities caps = DesiredCapabilities.firefox();
    	caps.setCapability("nativeEvents", false);
    	wd = new FirefoxDriver(caps);
        //wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        
    	//DesiredCapabilities caps = DesiredCapabilities.chrome();
    	//caps.setCapability("platform", "Windows 7");
    	//caps.setCapability("version", "");
         // wd = new RemoteWebDriver(new URL("http://isvitlychenko:71da6c65-7275-486f-8516-11f76f5f1c47@ondemand.saucelabs.com:80/wd/hub"),
    		//			  caps);
        }
    
    
    @Test
    public void validOrderPlacement() {
        openHomePage();
         
        List <WebElement> propositions = wd.findElements(By.xpath("/html/body/div[7]/div/div/a/div[3]/span"));

        
        for (WebElement proposition : propositions){
	        String proposition_name=proposition.getText();
	        System.out.println("Available propositions: " + proposition_name);
	        }
        
        propositions.get(0).click();
       
        if (!wd.findElement(By.tagName("html")).getText().contains("Please fill in everything marked with an asterisk")) {
            System.out.println("Form invitation text is not found");
        }
        
        fillInOrderDetails();
        submitOrder();
        
        if (!wd.findElement(By.tagName("html")).getText().contains("Thanks. Your SIM will be with you in 2-4 working days")) {
            System.out.println("Thank you copy is not shown");
        }
    }

	private void submitOrder() {
		wd.findElement(By.id("submit_order")).click();
	}

	private void fillInOrderDetails() {
		wd.findElement(By.xpath("//label[@for='number_of_sims_2']//span[.='2 SIM cards']")).click();
		wd.findElement(By.id("number_of_sims_2")).click();
		if (!wd.findElement(By.xpath("//div[@id='order_form']/fieldset/div[1]/select/option[5]")).isSelected()) {
		    wd.findElement(By.xpath("//div[@id='order_form']/fieldset/div[1]/select/option[5]")).click();
		}
		wd.findElement(By.id("customer_first_name")).click();
		wd.findElement(By.id("customer_first_name")).clear();
		wd.findElement(By.id("customer_first_name")).sendKeys("Test");
		wd.findElement(By.id("customer_last_name")).click();
		wd.findElement(By.id("customer_last_name")).clear();
		wd.findElement(By.id("customer_last_name")).sendKeys("Testing");
		wd.findElement(By.id("customer_email")).click();
		wd.findElement(By.id("customer_email")).clear();
		wd.findElement(By.id("customer_email")).sendKeys("agencyrepublictechtest@gmail.com");
		wd.findElement(By.id("customer_mobile_number")).click();
		wd.findElement(By.id("customer_mobile_number")).clear();
		wd.findElement(By.id("customer_mobile_number")).sendKeys("07511154254");
		wd.findElement(By.id("postcode")).click();
		wd.findElement(By.id("postcode")).clear();
		wd.findElement(By.id("postcode")).sendKeys("SW113BZ");
        wd.findElement(By.id("paf_lookup_submit")).click();
        new WebDriverWait (wd,30).until(ExpectedConditions.presenceOfElementLocated(By.id("order_form_address_list")));
        if (!wd.findElement(By.id("order_form_address_list")).getAttribute("value").equals("Choose address")) {
            System.out.println("Address options drop-down is not shown");
        }
        if (!wd.findElement(By.xpath("//div[@id='order_form_address_results']/select/option[3]")).isSelected()) {
            wd.findElement(By.xpath("//div[@id='order_form_address_results']/select/option[3]")).click();
        }
        if (!wd.findElement(By.id("address_1")).getAttribute("value").equals("Agency Republic, The Glassmill")) {
            System.out.println("Wrong (not AR) addess is selected");
        }
        if (!wd.findElement(By.id("customer_has_accepted_terms_1")).isSelected()) {
            wd.findElement(By.id("customer_has_accepted_terms_1")).click();
        }
	}

	
	
	private void openHomePage() {
		wd.get(url);
	}
    
    @AfterClass
    public void tearDown() {
        wd.quit();
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
