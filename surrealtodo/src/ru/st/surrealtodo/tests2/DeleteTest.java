package ru.st.surrealtodo.tests2;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Iterator;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

import ru.st.surrealtodo.fw.AppObject;
import ru.st.surrealtodo.fw.ListOf;

public class DeleteTest<Parent extends AppObject<Parent,Child>, Child extends AppObject<Child,?>> {
  
  private ParentProvider<Parent> parentProvider;

  private Parent parent;
  private ListOf<Child> objectsBefore;

  private int howMany = 1;

  public DeleteTest() {
  }

  public DeleteTest<Parent, Child> withParentProvider(ParentProvider<Parent> parentProvider) {
    this.parentProvider = parentProvider;
    return this;
  }
  
  public DeleteTest<Parent, Child> delete(int howMany) {
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

  @DataProvider(name = "deleteProvider")
  public Iterator<Object[]> objectProvider() {
    int needMore = howMany - parent.getChildren().size();
    for (int i = 0; i < needMore; i++) {
      parent.createChild();
    }
    ListOf<Child> children = parent.getChildren();
    List<Object[]> data = Lists.newArrayList();
    for (int i = 0; i < howMany; i++) {
      Child selected = children.getSome();
      data.add(new Object[]{selected});
      children.remove(selected);
    }
    return data.iterator();
  }

  @BeforeMethod
  public void savePreState() {
    objectsBefore = getAllObjects();
  }

  @Test(enabled = true, dataProvider = "deleteProvider")
  public void canDelete(Child objectToDelete) {
    objectToDelete.delete();

    assertThat(getAllObjects(),
        equalTo(objectsBefore.without(objectToDelete)));
  }
}
