package ru.st.surrealtodo.tests2;

import java.util.List;

import org.testng.annotations.Factory;
import org.testng.collections.Lists;

import ru.st.surrealtodo.fw.AppObject;
import ru.st.surrealtodo.testlib.NowDateProvider;
import ru.st.surrealtodo.testlib.Provider;
import ru.st.surrealtodo.testlib.StringProvider;

public class CRUDTestFactory extends TestSuiteBase {

  private Provider shortTextProvider = new StringProvider(10);
  private Provider now = new NowDateProvider();

  @Factory
  public Object[] testCRUD() {

    List<Object> tests = Lists.newArrayList();
    tests.addAll(crudTests(new TopLevelProvider()));
    tests.addAll(crudTests(new TabProvider()));
    tests.addAll(crudTests(new PageProvider()));
    tests.addAll(crudTests(new ListProvider()));
    return (Object[]) tests.toArray(new Object[tests.size()]);
  }
  
  private <Parent extends AppObject<Parent,Child>, Child extends AppObject<Child,?>> List<Object>
      crudTests(ParentProvider<Parent> parentProvider)
  {
    List<Object> tests = Lists.newArrayList();
    tests.add(new CreateTest<Parent, Child>().withParentProvider(parentProvider));
    tests.add(new ChangeTextTest<Parent, Child>().withParentProvider(parentProvider).changeText(shortTextProvider));
    tests.add(new ChangeDateTest<Parent, Child>().withParentProvider(parentProvider).changeDate(now));
    tests.add(new DeleteTest<Parent, Child>().withParentProvider(parentProvider));
    return tests;
  }
}
