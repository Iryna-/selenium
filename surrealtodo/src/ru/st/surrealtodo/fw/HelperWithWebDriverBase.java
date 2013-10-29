package ru.st.surrealtodo.fw;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelperWithWebDriverBase extends HelperBase {
	
	private static Logger log = LoggerFactory.getLogger(HelperWithWebDriverBase.class);

	protected WebDriver driver;
	private String mainWindow;

	public HelperWithWebDriverBase(Application manager) {
		super(manager);
		driver = manager.getWebDriverHelper().getDriver();
		mainWindow = driver.getWindowHandle();
	}

	protected void openUrl(String relativeUrl) {
		driver.get(baseUrl() + relativeUrl);
		waitFor(ajaxComplete());
	}

	protected String baseUrl() {
		return manager.getProperty("baseUrl");
	}

	protected void type(By locator, String text) {
		WebElement element = findElement(locator);
    element.click();
		element.clear();
		element.sendKeys(text);
    waitFor(ajaxComplete());
	}

  protected void typeAsync(By locator, String text) {
    WebElement element = findElement(locator);
    element.clear();
    element.sendKeys(text);
  }

	protected void click(By locator) {
		findElement(locator).click();
		waitFor(ajaxComplete());
	}

	protected void clickAsync(By locator) {
		findElement(locator).click();
	}

  protected void click(WebElement element) {
    element.click();
    waitFor(ajaxComplete());
  }

  protected void clickAsync(WebElement element) {
    element.click();
  }

  protected void contextClick(By locator) {
    new Actions(driver).contextClick(findElement(locator)).perform();
    waitFor(ajaxComplete());
  }

  protected void doubleClick(By locator) {
    new Actions(driver).doubleClick(findElement(locator)).perform();
    waitFor(ajaxComplete());
  }

  protected void mouseOver(By locator) {
    new Actions(driver).moveToElement(findElement(locator)).perform();
    waitFor(ajaxComplete());
  }

  protected void dragAndDrop(By locatorSrc, By locatorTgt) {
    new Actions(driver)
      .clickAndHold(findElement(locatorSrc))
      .moveToElement(findElement(locatorTgt))
      .release().perform();
    waitFor(ajaxComplete());
  }

	protected WebElement findElement(By locator) {
	  return waitFor(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	protected WebElement findOneOf(final By... locators) {
		return waitFor(oneOf(locators));
	}

	protected boolean isElementPresent(By locator) {
	  return findElements(locator).size() > 0;
	}

	protected List<WebElement> findElements(By locator) {
		return driver.findElements(locator);
	}

  protected String getInnerHtml(WebElement element) {
    JavascriptExecutor jsdriver = (JavascriptExecutor) driver;
    return (String) jsdriver.executeScript("return arguments[0].innerHTML", element);
  }

	protected void switchToNewWindow() {
		driver.switchTo().window(waitFor(newWindow()));
	}

	protected void switchToMainWindow() {
		driver.switchTo().window(mainWindow);
	}

	protected void pause(long pause) {
		try {
			Thread.sleep(pause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected <T> T waitFor(ExpectedCondition<T> condition) {
		log.debug("Start waiting for {}", condition);
		try {
			return new WebDriverWait(driver, manager.getTimeout()).until(condition);
		} catch (TimeoutException e) {
			log.debug("Timeout, there was no {}", condition);
			throw e;
		} finally {
			log.debug("Stop waiting for {}", condition);
		}
	}

	protected ExpectedCondition<Boolean> ajaxComplete() {
		return new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver d) {
				long x = (Long) ((JavascriptExecutor) d).executeScript(
				    "return window.jQuery.active", new Object[]{});
				return x == 0;
		  	}};
	}
	
	protected ExpectedCondition<String> newWindow() {
		return new ExpectedCondition<String>(){
			public String apply(WebDriver d) {
				Set<String> handles = d.getWindowHandles();
				handles.remove(mainWindow);
				return handles.size() > 0 ? handles.iterator().next() : null;
		  	}};
	}

	private ExpectedCondition<WebElement> oneOf(final By... locators) {
		return new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver d) {
				for (By locator : locators) {
					try {
						WebElement element = driver.findElement(locator);
						return element;
					} catch (NoSuchElementException e) {
					}
				}
				return null;
			}
		};
	}

}
