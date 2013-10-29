package ru.st.surrealtodo.tests;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.testng.annotations.Test;

import ru.st.surrealtodo.fw.ListOf;
import ru.st.surrealtodo.fw.Page;
import ru.st.surrealtodo.fw.Tab;
import ru.st.surrealtodo.fw.TodoList;

public class TestDnD extends TestBase {
  
  @Test(enabled = true)
  public void canChangeOrderOfTabs() {
    // prepare state
    ListOf<Tab> tabs = getAtLeastTwoTabs();
    Tab tab1 = tabs.get(0);
    Tab tab2 = tabs.get(tabs.size()-1);
    // save state
    ListOf<Tab> tabsBefore = app.getTabs();
    // do action
    tab2.moveBefore(tab1);
    // perform checks
    assertThat(app.getTabs(), equalTo(tabsBefore.without(tab2).withPrepended(tab2)));
  }

  @Test(enabled = true)
  public void canChangeOrderOfPages() {
    // prepare state
    Tab tab = getSomeTab();
    ListOf<Page> pages = getAtLeastTwoPages(tab);
    Page page1 = pages.get(0);
    Page page2 = pages.get(pages.size()-1);
    // save state
    ListOf<Page> pagesBefore = tab.getPages();
    // do action
    page2.moveBefore(page1);
    // perform checks
    assertThat(tab.getPages(), equalTo(pagesBefore.without(page2).withPrepended(page2)));
  }

  @Test(enabled = true)
  public void canMovePagesToOtherTabs() {
    // prepare state
    ListOf<Tab> tabs = getAtLeastTwoTabs();
    Tab tab1 = tabs.get(0);
    Tab tab2 = tabs.get(tabs.size()-1);
    Page page = getSomePageOn(tab2);
    // save state
    ListOf<Page> pagesBeforeOnTab1 = tab1.getPages();
    ListOf<Page> pagesBeforeOnTab2 = tab2.getPages();
    // do action
    page.moveTo(tab1);
    // perform checks
    assertThat(tab2.getPages(), equalTo(pagesBeforeOnTab2.without(page)));
    assertThat(tab1.getPages(), equalTo(pagesBeforeOnTab1.withPrepended(page)));
  }

  @Test(enabled = true)
  public void canMoveListsToOtherColumns() {
    TodoList list = getSomeList();
    int column = list.columnNo() > 2 ? 1 : list.columnNo()+1;
    list.moveToColumn(column);
  }
}
