package ru.st.surrealtodo.tests2;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Iterator;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

import ru.st.surrealtodo.fw.AppObject;
import ru.st.surrealtodo.fw.ListOf;
import ru.st.surrealtodo.testlib.Provider;
import ru.st.surrealtodo.testlib.StringProvider;
import ru.st.testng.TracingListener;

@Listeners({TracingListener.class})
public class ChangeTextTest<Parent extends AppObject<Parent,Child>, Child extends AppObject<Child,?>> {
  
  private ParentProvider<Parent> parentProvider;

  private Parent parent;
  private ListOf<Child> objectsBefore;

  private int howMany = 1;
  private Provider textProvider = new StringProvider(10);

  public ChangeTextTest() {
  }

  public ChangeTextTest<Parent, Child> withParentProvider(ParentProvider<Parent> parentProvider) {
    this.parentProvider = parentProvider;
    return this;
  }
  
  public ChangeTextTest<Parent, Child> changeText(Provider textProvider) {
    this.textProvider = textProvider;
    return this;
  }
  
  public ChangeTextTest<Parent, Child> howMany(int howMany) {
    this.howMany = howMany;
    return this;
  }
  
  @BeforeClass
  public void initParent() {
    parent = parentProvider.getObject();
  }

  private ListOf<Child> getAllObjects() {
    return parent.getChildren();
  };

  private Child getSomeObject() {
    ListOf<Child> children = parent.getChildren();
    return children.size() > 0 ? children.getSome() : parent.createChild();
  };
  
  @DataProvider(name = "changeTextProvider")
  public Iterator<Object[]> objectAndStringProvider() {
    List<Object[]> data = Lists.newArrayList();
    for (int i = 0; i < howMany; i++) {
      data.add(new Object[]{getSomeObject(), textProvider.getData()});
    }
    return data.iterator();
  }

  @BeforeMethod
  public void savePreState() {
    objectsBefore = getAllObjects();
  }

  @Test(enabled = true, dataProvider = "changeTextProvider")
  public void canChangeTextOf(Child objectToModify, String newText) {
    Child modifiedObject = objectToModify.changeTextTo(newText);

    assertThat(modifiedObject.text(),
        is(newText));
    assertThat(getAllObjects().without(modifiedObject),
        equalTo(objectsBefore.without(objectToModify)));
  }
}
