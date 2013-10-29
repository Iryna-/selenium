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
import ru.st.surrealtodo.fw.Page;
import ru.st.surrealtodo.testlib.Provider;
import ru.st.surrealtodo.testlib.StringProvider;
import ru.st.testng.TracingListener;

@Listeners({TracingListener.class})
public class CreateAndRenameTest<Parent extends AppObject<Parent,Child>, Child extends AppObject<Child,?>>
    implements ParentProvider<Child>
{
  
  private ParentProvider<Parent> parentProvider;

  private Parent parent;
  private ListOf<Child> objectsBefore;
  
  private Provider textProvider = new StringProvider(10);

  private Child newObject;

  public CreateAndRenameTest() {
  }

  public CreateAndRenameTest<Parent, Child> withParentProvider(ParentProvider<Parent> parentProvider) {
    this.parentProvider = parentProvider;
    return this;
  }
  
  public CreateAndRenameTest<Parent, Child> changeText(Provider textProvider) {
    this.textProvider = textProvider;
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
  
  @DataProvider(name = "createAndRenameProvider")
  public Iterator<Object[]> defaultTextProvider() {
    List<Object[]> data = Lists.newArrayList();
    data.add(new Object[]{parentProvider.newObjectDefaultText(), textProvider.getData()});
    return data.iterator();
  }

  @BeforeMethod
  public void savePreState() {
    objectsBefore = getAllObjects();
  }

  @Test(enabled = true, dataProvider = "createAndRenameProvider")
  public void canCreateAndRenameObject(String parent, String text) {
    newObject = createObject().changeTextTo(text);

    assertThat(newObject.text(),
        is(text));
    assertThat(getAllObjects(),
        equalTo(parentProvider.newObjectPrepending()
            ? objectsBefore.withPrepended(newObject)
            : objectsBefore.withAppended(newObject)));
  }

  @Override
  public Child getObject() {
    return newObject;
  }

  @Override
  public String newObjectDefaultText() {
    return "Child of " + newObject.getClass().getSimpleName();
  }

  @Override
  public boolean newObjectPrepending() {
    return newObject instanceof Page;
  }
}
