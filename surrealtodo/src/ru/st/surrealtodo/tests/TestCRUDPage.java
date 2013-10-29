package ru.st.surrealtodo.tests;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.testng.annotations.Test;

import ru.st.surrealtodo.fw.ListOf;
import ru.st.surrealtodo.fw.Page;
import ru.st.surrealtodo.fw.Tab;

public class TestCRUDPage extends TestBase {
  
  @Test(enabled = true)
  public void canCreatePage() {
    // prepare state
    Tab tab = getSomeTab();
    // save state
    ListOf<Page> pagesBefore = tab.getPages();
    // do action
    Page newPage = tab.createPage();
    // perform checks
    assertThat(newPage.text(), is("New Page"));
    assertThat(tab.getPages(), equalTo(pagesBefore.withAppended(newPage)));
  }

  @Test(enabled = true)
  public void canRenamePage() {
    // prepare state
    Page page = getSomePage();
    Tab tab = page.getTab();
    // save state
    ListOf<Page> pagesBefore = tab.getPages();
    // do action
    String newName = randomNonEmptyString(10);
    Page renamedPage = page.changeTextTo(newName);
    // perform checks
    assertThat(renamedPage.text(), is(newName));
    assertThat(tab.getPages().without(renamedPage), equalTo(pagesBefore.without(page)));
  }

  @Test(enabled = true)
  public void canDeletePage() {
    // prepare state
    Page page = getSomePage();
    Tab tab = page.getTab();
    // save state
    ListOf<Page> pagesBefore = tab.getPages();
    // do action
    page.delete();
    // perform checks
    assertThat(tab.getPages(), equalTo(pagesBefore.without(page)));
  }

  @Test(enabled = true)
  public void canChangeCreationDate() {
    // prepare state
    Page page = getSomePage();
    Tab tab = page.getTab();
    // save state
    ListOf<Page> pagesBefore = tab.getPages();
    // do action
    String now = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
    Page modifiedPage = page.changeCreationDateTo(now);
    // perform checks
    assertThat(modifiedPage.creationDate(), is(now));
    assertThat(tab.getPages().without(modifiedPage), equalTo(pagesBefore.without(page)));
  }

}
