package ru.st.surrealtodo.tests2;

import java.util.List;

import org.testng.annotations.Factory;
import org.testng.collections.Lists;

import ru.st.surrealtodo.fw.Page;
import ru.st.surrealtodo.fw.Tab;
import ru.st.surrealtodo.fw.TabHelper;
import ru.st.surrealtodo.fw.TodoItem;
import ru.st.surrealtodo.fw.TodoList;
import ru.st.surrealtodo.testlib.Provider;
import ru.st.surrealtodo.testlib.StringProvider;

public class CreationTestFactory extends TestSuiteBase {

  private Provider shortTextProvider = new StringProvider(10);
  int tabCount = 2;
  int pageCount = 2;
  int listCount = 2;
  int itemCount = 2;

  @Factory
  public Object[] testCreate() {
    List<Object> tests = Lists.newArrayList();
    for (int i = 0; i < tabCount; i++) {
      CreateAndRenameTest<TabHelper, Tab> tabTest = new CreateAndRenameTest<TabHelper, Tab>()
        .withParentProvider(new TopLevelProvider()).changeText(shortTextProvider); 
      tests.add(tabTest);
      for (int j = 0; j < pageCount; j++) {
        CreateAndRenameTest<Tab, Page> pageTest = new CreateAndRenameTest<Tab, Page>()
          .withParentProvider(tabTest).changeText(shortTextProvider);
        tests.add(pageTest);
        for (int k = 0; k < listCount; k++) {
          CreateAndRenameTest<Page, TodoList> listTest = new CreateAndRenameTest<Page, TodoList>()
              .withParentProvider(pageTest).changeText(shortTextProvider);
          tests.add(listTest);
          for (int l = 0; l < itemCount; l++) {
            CreateAndRenameTest<TodoList, TodoItem> itemTest = new CreateAndRenameTest<TodoList, TodoItem>()
              .withParentProvider(listTest).changeText(shortTextProvider);
            tests.add(itemTest);
          }
        }
      }
    }
    return (Object[]) tests.toArray(new Object[tests.size()]);
  }
}
