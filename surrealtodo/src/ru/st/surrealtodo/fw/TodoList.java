package ru.st.surrealtodo.fw;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Iterables;

public class TodoList extends AppObject<TodoList,TodoItem> {

  private Page page;
  private int columnNo;

  public TodoList(Page page) {
    super(page.manager);
    this.page = page;
  }
  
  public Page getPage() {
    return page;
  }
  
  public TodoList withColumnNo(int columnNo) {
    this.columnNo = columnNo;
    return this;
  }
  
  public int columnNo() {
    return columnNo;
  }

  private By listSelector() {
    return By.cssSelector("ul#" + id);
  }

  private By listTitleSelector() {
    return By.cssSelector("div#" + id);
  }

  public TodoList changeTextTo(String newText) {
    page.select();
    contextClick(listTitleSelector());
    click(By.cssSelector("ul#menuList li#edit"));
    type(By.cssSelector("div#" + id + " div.listName input"), newText + Keys.ENTER);
    return page.updateCachedChildren().getById(this.id);
  }

  public void delete() {
    page.select();
    contextClick(listTitleSelector());
    click(By.cssSelector("ul#menuList li#delete"));
    click(findElement(By.xpath("//div[@id='dialog-confirm-delete-list']/.."))
        .findElement(By.cssSelector("div.ui-dialog-buttonpane button:nth-of-type(2)")));
    page.updateCachedChildren();
  }

  public TodoList changeCreationDateTo(String creationDate) {
    page.select();
    contextClick(listTitleSelector());
    mouseOver(By.cssSelector("ul#menuList li#listCreated"));
    type(By.cssSelector("ul#menuList input.dateCreatedInput"), creationDate + Keys.ENTER);
    return page.updateCachedChildren().getById(this.id);
  }

  public TodoList moveToColumn(int columnNo) {
     dragAndDrop(listTitleSelector(), By.cssSelector("ul#Column-"+columnNo));
     return page.updateCachedChildren().getById(this.id);
  }

  @Override
  protected ListOf<TodoItem> updateCachedChildrenInternal() {
    page.select();
    waitFor(ajaxComplete());
    List<WebElement> elements = findElement(listSelector()).findElements(By.cssSelector("li.item"));
    ListOf<TodoItem> cachedItems = new ListOf<TodoItem>();
    for (WebElement element : elements) {
      if (element.isDisplayed()) {
        TodoItem item = new TodoItem(this)
          .withId(element.getAttribute("id"))
          .withText(element.getText())
          .withCreationDate(getInnerHtml(element.findElement(By.cssSelector("div.dateCreated"))));
        cachedItems.add(item);
      }
    }
    return cachedItems;
  }

  public TodoItem createItem() {
    page.select();
    ListOf<TodoItem> itemsBefore = getChildren();
    findElement(listTitleSelector()).findElement(By.cssSelector("abbr.newItem")).click();
    updateCachedChildren();
    return Iterables.find(getChildren(), not(in(itemsBefore)));
  }

  @Override
  public TodoItem createChild() {
    return createItem();
  }

}
