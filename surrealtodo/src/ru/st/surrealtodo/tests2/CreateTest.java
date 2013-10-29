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
import ru.st.testng.TracingListener;

@Listeners({TracingListener.class})
public class CreateTest<Parent extends AppObject<Parent,Child>, Child extends AppObject<Child,?>> {
  
  private ParentProvider<Parent> parentProvider;

  private Parent parent;
  private ListOf<Child> objectsBefore;
  
  private int howMany = 1;

  public CreateTest() {
  }

  public CreateTest<Parent, Child> withParentProvider(ParentProvider<Parent> parentProvider) {
    this.parentProvider = parentProvider;
    return this;
  }
  
  public CreateTest<Parent, Child> howMany(int howMany) {
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

  private Child createObject() {
    return parent.createChild();
  };
  
  @DataProvider(name = "createProvider")
  public Iterator<Object[]> defaultTextProvider() {
    List<Object[]> data = Lists.newArrayList();
    for (int i = 0; i < howMany; i++) {
      data.add(new Object[]{parentProvider.newObjectDefaultText()});
    }
    return data.iterator();
  }

  @BeforeMethod
  public void savePreState() {
    objectsBefore = getAllObjects();
  }

  @Test(enabled = true, dataProvider = "createProvider")
  public void canCreateObject(String defaultText) {
    Child newObject = createObject();

    assertThat(newObject.text(),
        is(defaultText));
    assertThat(getAllObjects(),
        equalTo(parentProvider.newObjectPrepending()
            ? objectsBefore.withPrepended(newObject)
            : objectsBefore.withAppended(newObject)));
  }
}
