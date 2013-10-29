package ru.st.surrealtodo.fw;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Iterables;

public class Page extends AppObject<Page,TodoList> {

  private Tab tab;

  public Page(Tab tab) {
    super(tab.manager);
    this.tab = tab;
  }
  
  public Tab getTab() {
    return tab;
  }
  
  private By pageSelector() {
    return By.cssSelector("li#" + id);
  }

  public boolean isSelected() {
    tab.select();
    return findElement(pageSelector()).findElement(By.tagName("a")).getAttribute("class").contains("activePage");
  }

  public Page select() {
    tab.select();
    if (! isSelected()) {
      click(pageSelector());
    }
    return this;
  }
  
  public Page changeTextTo(String newText) {
    tab.select();
    contextClick(pageSelector());
    click(By.cssSelector("ul#menuPage li#edit"));
    type(By.cssSelector("li#" + id + " span.pageName input"), newText + Keys.ENTER);
    return tab.updateCachedChildren().getById(this.id);
  }

  public void delete() {
    tab.select();
    contextClick(pageSelector());
    click(By.cssSelector("ul#menuPage li#delete"));
    click(findElement(By.xpath("//div[@id='dialog-confirm-delete-page']/.."))
        .findElement(By.cssSelector("div.ui-dialog-buttonpane button:nth-of-type(2)")));
    tab.updateCachedChildren();
  }

  public Page changeCreationDateTo(String creationDate) {
    tab.select();
    contextClick(pageSelector());
    mouseOver(By.cssSelector("ul#menuPage li#pageCreated"));
    type(By.cssSelector("ul#menuPage input.dateCreatedInput"), creationDate + Keys.ENTER);
    return tab.updateCachedChildren().getById(this.id);
  }

  public ListOf<TodoList> getLists() {
    return getChildren();
  }

  @Override
  protected ListOf<TodoList> updateCachedChildrenInternal() {
    this.select();
    waitFor(ajaxComplete());
    ListOf<TodoList> cachedLists = new ListOf<TodoList>();
    List<WebElement> columns = findElements(By.cssSelector("div#tabContent ul.column"));
    for (int i = 1; i <= columns.size(); i++) {
      List<WebElement> elements = columns.get(i-1).findElements(By.cssSelector("div#tabContent div.listTitle"));
      for (WebElement element : elements) {
        if (element.isDisplayed()) {
          TodoList list = new TodoList(this)
            .withId(element.getAttribute("id"))
            .withText(element.getText())
            .withColumnNo(i)
            .withCreationDate(getInnerHtml(element.findElement(By.cssSelector("div.listCreated"))));
          cachedLists.add(list);
        }
      }
    }
    return cachedLists;
  }

  public TodoList createList() {
    this.select();
    ListOf<TodoList> listsBefore = getChildren();
    findElement(By.cssSelector("div#tabs abbr.newList")).click();
    updateCachedChildren();
    return Iterables.find(getChildren(), not(in(listsBefore)));
  }

  public void moveBefore(Page other) {
    tab.select();
    dragAndDrop(pageSelector(), other.pageSelector());
    tab.updateCachedChildren();
  }

  public void moveTo(Tab other) {
    tab.select();
    dragAndDrop(pageSelector(), other.tabSelector());
    tab.updateCachedChildren();
    other.invalidateCachedPages();
  }

  @Override
  public TodoList createChild() {
    return createList();
  }
}
