package ru.st.surrealtodo.fw;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Iterables;

public class Tab extends AppObject<Tab,Page> {

  public Tab(Application manager) {
    super(manager);
  }
  
  protected By tabSelector() {
    return By.cssSelector("li#" + id);
  }
  
  public boolean isSelected() {
    manager.navigateTo().notebook();
    return findElement(tabSelector()).findElement(By.tagName("a")).getAttribute("class").contains("activeTab");
  }

  public Tab select() {
    manager.navigateTo().notebook();
    if (! isSelected()) {
      click(tabSelector());
    }
    return this;
  }
  
  public Tab changeTextTo(String newText) {
    manager.navigateTo().notebook();
    contextClick(tabSelector());
    click(By.cssSelector("ul#menuTab li#edit"));
    type(By.cssSelector("li#" + id() + " div.tabName input"), newText + Keys.ENTER);
    return manager.getTabHelper().updateCachedChildren().getById(this.id);
  }

  public void delete() {
    manager.navigateTo().notebook();
    contextClick(tabSelector());
    click(By.cssSelector("ul#menuTab li#delete"));
    click(findElement(By.xpath("//div[@id='dialog-confirm-delete-tab']/.."))
        .findElement(By.cssSelector("div.ui-dialog-buttonpane button:nth-of-type(2)")));
    manager.getTabHelper().updateCachedChildren();
  }

  public Tab changeCreationDateTo(String creationDate) {
    manager.navigateTo().notebook();
    contextClick(tabSelector());
    mouseOver(By.cssSelector("ul#menuTab li#tabCreated"));
    type(By.cssSelector("ul#menuTab input.dateCreatedInput"), creationDate + Keys.ENTER);
    return manager.getTabHelper().updateCachedChildren().getById(this.id);
  }

  public ListOf<Page> getPages() {
    return getChildren();
  }

  protected void invalidateCachedPages() {
    cachedChildren = null;
  }

  @Override
  protected ListOf<Page> updateCachedChildrenInternal() {
    this.select();
    waitFor(ajaxComplete());
    List<WebElement> elements = findElements(By.cssSelector("ul#pageList li.pageItem"));
    ListOf<Page> cachedPages = new ListOf<Page>();
    for (WebElement element : elements) {
      if (element.findElement(By.tagName("a")).isDisplayed()) {
        Page page = new Page(this)
          .withId(element.getAttribute("id"))
          .withText(element.getText())
          .withCreationDate(getInnerHtml(element.findElement(By.cssSelector("div.pageCreated"))));
        cachedPages.add(page);
      }
    }
    return cachedPages;
  }

  public Page createPage() {
    this.select();
    ListOf<Page> pagesBefore = getChildren();
    findElement(By.cssSelector("div#pages a.newPage")).click();
    updateCachedChildren(); 
    return Iterables.find(getChildren(), not(in(pagesBefore)));
  }

  public Tab moveBefore(Tab other) {
    manager.navigateTo().notebook();
    dragAndDrop(tabSelector(), other.tabSelector());
    return manager.getTabHelper().updateCachedChildren().getById(this.id);
  }

  @Override
  public Page createChild() {
    return createPage();
  }
}
