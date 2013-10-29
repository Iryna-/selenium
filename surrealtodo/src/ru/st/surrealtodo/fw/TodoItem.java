package ru.st.surrealtodo.fw;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class TodoItem extends AppObject<TodoItem,TodoItem> {

  private TodoList list;

  public TodoItem(TodoList list) {
    super(list.manager);
    this.list = list;
  }
  
  public TodoList getList() {
    return list;
  }
  
  private By itemSelector() {
    return By.cssSelector("li#" + id());
  }

  public TodoItem changeTextTo(String newText) {
    list.getPage().select();
    doubleClick(itemSelector());
    type(By.cssSelector("li#" + id() + " textarea"), newText + Keys.ENTER);
    return list.updateCachedChildren().getById(this.id);
  }

  public void delete() {
    list.getPage().select();
    contextClick(itemSelector());
    click(By.cssSelector("ul#menuItem li#delete"));
    list.updateCachedChildren();
  }

  public TodoItem changeCreationDateTo(String creationDate) {
    list.getPage().select();
    contextClick(itemSelector());
    mouseOver(By.cssSelector("ul#menuItem li#itemCreated"));
    type(By.cssSelector("ul#menuItem input.dateCreatedInput"), creationDate + Keys.ENTER);
    return list.updateCachedChildren().getById(this.id);
  }

  @Override
  protected ListOf<TodoItem> updateCachedChildrenInternal() {
    throw new UnsupportedOperationException();
  }

  @Override
  public TodoItem createChild() {
    throw new UnsupportedOperationException();
  }
}
