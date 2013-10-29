package com.example.tests;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Test1 {
  
  protected RemoteWebDriver driver;
  
  @Before
  public void startDriver() {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
  }
  
  @After
  public void stopDriver() {
    driver.quit();
    driver = null;
  }
  
  @Test
  public void test1() {
    driver.get("http://selenium2.ru/");
    WebElement link2 = driver.findElement(By.cssSelector("ul.menu li.item-104 a"));
    assertEquals(link2.getText(), "ƒŒ ”Ã≈Õ“¿÷»ﬂ");
    link2.click();
    driver.findElement(By.cssSelector("ul.menu"));
  }

}
