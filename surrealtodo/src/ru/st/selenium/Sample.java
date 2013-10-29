package ru.st.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class Sample {
  
  public static void main(String[] args) {
    WebDriver driver = new FirefoxDriver();
    driver.findElement(By.id("myelement")).getCssValue("color");
  }

}
