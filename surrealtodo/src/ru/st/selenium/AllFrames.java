package ru.st.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class AllFrames implements SearchContext {

  private final WebDriver driver;

  public AllFrames(WebDriver driver) {
    this.driver = driver;
  }

  @Override
  public WebElement findElement(By locator) {
    WebElement found = null;
    List<WebElement> frames = driver.findElements(By.tagName("iframe"));
    for (WebElement frame : frames) {
      driver.switchTo().frame(frame);
      try {
        found = driver.findElement(locator);
        break;
      } catch (WebDriverException e) {
      }
    }
    if (found == null) {
      driver.switchTo().defaultContent();
    }
    return found;
  }

  @Override
  public List<WebElement> findElements(By locator) {
    List<WebElement> found = driver.findElements(locator);
    List<WebElement> frames = driver.findElements(By.tagName("iframe"));
    for (WebElement frame : frames) {
      driver.switchTo().frame(frame);
      found.addAll(driver.findElements(locator));
    }
    driver.switchTo().defaultContent();
    return found;
  }

}
