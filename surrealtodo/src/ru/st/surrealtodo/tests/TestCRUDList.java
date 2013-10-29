package ru.st.surrealtodo.tests;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.testng.annotations.Test;

import ru.st.surrealtodo.fw.ListOf;
import ru.st.surrealtodo.fw.Page;
import ru.st.surrealtodo.fw.TodoList;

public class TestCRUDList extends TestBase {
  
  @Test(enabled = true)
  public void canCreateList() {
    // prepare state
    Page page = getSomePage();
    // save state
    ListOf<TodoList> listsBefore = page.getLists();
    // do action
    TodoList newList = page.createList();
    // perform checks
    assertThat(newList.text(), is("New List"));
    assertThat(page.getLists(), equalTo(listsBefore.withAppended(newList)));
  }

  @Test(enabled = true)
  public void canRenameList() {
    // prepare state
    TodoList list = getSomeList();
    Page page = list.getPage();
    // save state
    ListOf<TodoList> listsBefore = page.getLists();
    // do action
    String newName = randomNonEmptyString(10);
    TodoList renamedList = list.changeTextTo(newName);
    // perform checks
    assertThat(renamedList.text(), equalTo(newName));
    assertThat(page.getLists().without(renamedList), equalTo(listsBefore.without(list)));
  }

  @Test(enabled = true)
  public void canDeleteList() {
    TodoList list = getSomeList();
    list.delete();
    assertThat(list.getPage().getLists().getById(list.id()), is(nullValue()));
  }

  @Test(enabled = true)
  public void canChangeListCreationDate() {
    TodoList list = getSomeList();
    String now = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
    TodoList modifiedList = list.changeCreationDateTo(now);
    assertThat(modifiedList.creationDate(), equalTo(now));
  }

}
