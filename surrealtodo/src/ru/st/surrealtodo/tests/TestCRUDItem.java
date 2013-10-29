package ru.st.surrealtodo.tests;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.testng.annotations.Test;

import ru.st.surrealtodo.fw.TodoItem;

public class TestCRUDItem extends TestBase {
  
  @Test(enabled = false)
  public void canCreateItem() {
    TodoItem newItem = getSomeList().createItem();
    assertThat(newItem.text(), equalTo("Double-click to edit"));
  }

  @Test(enabled = false)
  public void canEditItem() {
    TodoItem item = getSomeItem();
    String newText = randomNonEmptyString(50);
    TodoItem modifiedItem = item.changeTextTo(newText);
    assertThat(modifiedItem.text(), equalTo(newText));
  }

  @Test(enabled = false)
  public void canDeleteItem() {
    TodoItem item = getSomeItem();
    item.delete();
    assertThat(item.getList().getChildren().getById(item.id()), is(nullValue()));
  }

  @Test(enabled = true)
  public void canChangeCreationDate() {
    TodoItem item = getSomeItem();
    String now = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
    TodoItem modifiedItem = item.changeCreationDateTo(now);
    assertThat(modifiedItem.creationDate(), equalTo(now));
  }

}
