package ru.st.surrealtodo.fw;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Iterables;

public class TabHelper extends AppObject<TabHelper,Tab> {

  public TabHelper(Application manager) {
    super(manager);
  }

  public ListOf<Tab> getTabs() {
    return getChildren();
  }

  @Override
  protected ListOf<Tab> updateCachedChildrenInternal() {
    manager.navigateTo().notebook();
    waitFor(ajaxComplete());
    List<WebElement> elements = findElements(By.cssSelector("ul#tabRow li.tabItem"));
    ListOf<Tab> cachedTabs = new ListOf<Tab>();
    for (WebElement element : elements) {
      if (element.findElement(By.tagName("a")).isDisplayed()) {
        Tab tab = new Tab(manager)
          .withId(element.getAttribute("id"))
          .withText(element.getText())
          .withCreationDate(getInnerHtml(element.findElement(By.cssSelector("div.tabCreated"))));
        cachedTabs.add(tab);
      }
    }
    return cachedTabs;
  }

  public Tab createTab() {
    manager.navigateTo().notebook();
    ListOf<Tab> tabsBefore = getTabs();
    findElement(By.cssSelector("div#tabs abbr.newTab")).click();
    updateCachedChildren(); 
    return Iterables.find(getChildren(), not(in(tabsBefore)));
  }
  
  @Override
  public TabHelper changeTextTo(String newText) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TabHelper changeCreationDateTo(String newDate) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Tab createChild() {
    return createTab();
  }

  @Override
  public void delete() {
    throw new UnsupportedOperationException();
  }
}
