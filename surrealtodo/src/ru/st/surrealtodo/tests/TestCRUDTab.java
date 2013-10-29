package ru.st.surrealtodo.tests;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.testng.annotations.Test;

import ru.st.surrealtodo.fw.ListOf;
import ru.st.surrealtodo.fw.Tab;

public class TestCRUDTab extends TestBase {
  
  @Test(enabled = true)
  public void canCreateTab() {
    // save state
    ListOf<Tab> tabsBefore = app.getTabs();
    // do action
    Tab newTab = app.createTab();
    // perform checks
    assertThat(newTab.text(), is("New Tab"));
    assertThat(app.getTabs(), equalTo(tabsBefore.withAppended(newTab)));
  }

  @Test(enabled = true)
  public void canRenameTab() {
    // prepare state
    Tab tab = getSomeTab();
    // save state
    ListOf<Tab> tabsBefore = app.getTabs();
    // do action
    String newName = randomNonEmptyString(10);
    Tab renamedTab = tab.changeTextTo(newName);
    // perform checks
    assertThat(renamedTab.text(), is(newName));
    assertThat(app.getTabs().without(renamedTab), equalTo(tabsBefore.without(tab)));
  }

  @Test(enabled = true)
  public void canDeletePage() {
    // prepare state
    Tab tab = getSomeTab();
    // save state
    ListOf<Tab> tabsBefore = app.getTabs();
    // do action
    tab.delete();
    // perform checks
    assertThat(app.getTabs(), equalTo(tabsBefore.without(tab)));
  }

  @Test(enabled = true)
  public void canChangeCreationDate() {
    // prepare state
    Tab tab = getSomeTab();
    // save state
    ListOf<Tab> tabsBefore = app.getTabs();
    // do action
    String now = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
    Tab modifiedTab = tab.changeCreationDateTo(now);
    // perform checks
    assertThat(modifiedTab.creationDate(), is(now));
    assertThat(app.getTabs().without(modifiedTab), equalTo(tabsBefore.without(tab)));
  }
}
