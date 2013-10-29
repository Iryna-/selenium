package ru.st.surrealtodo.fw;

import org.openqa.selenium.By;

public class NavigationHelper extends HelperWithWebDriverBase {

  public NavigationHelper(Application manager) {
    super(manager);
  }

  public void notebook() {
    if (! isOnNotebookPage()) {
      findElement(By.cssSelector("div#header ul.headerMenu li:nth-of-type(1) a")).click();
    }
  }

  private boolean isOnNotebookPage() {
    return isElementPresent(By.cssSelector("div#pages"));
  }

  public void trash() {
    findElement(By.cssSelector("div#header ul.headerMenu li:nth-of-type(2) a")).click();
  }

  public void settings() {
    findElement(By.cssSelector("div#header ul.headerMenu li:nth-of-type(3) a")).click();
  }
}
