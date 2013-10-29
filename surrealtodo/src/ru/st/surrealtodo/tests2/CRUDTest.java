package ru.st.surrealtodo.tests2;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Iterator;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

import ru.st.surrealtodo.fw.AppObject;
import ru.st.surrealtodo.fw.ListOf;
import ru.st.surrealtodo.testlib.NowDateProvider;
import ru.st.surrealtodo.testlib.Provider;
import ru.st.surrealtodo.testlib.StringProvider;

public class CRUDTest<Parent extends AppObject<Parent,Child>, Child extends AppObject<Child,?>> {
  
  private ParentProvider<Parent> parentProvider;
  private String defaultText = "";
  private boolean prepending = false;

  private Parent parent;
  private ListOf<Child> objectsBefore;

  private int create = 1;
  private int changeText = 1;
  private Provider textProvider = new StringProvider(10);
  private int changeDate = 1;
  private Provider dateProvider = new NowDateProvider();
  private int delete = 1;

  public CRUDTest() {
  }

  public CRUDTest<Parent, Child> withParentProvider(ParentProvider<Parent> parentProvider) {
    this.parentProvider = parentProvider;
    return this;
  }
  
  public CRUDTest<Parent, Child> withNewObjectText(String defaultText) {
    this.defaultText = defaultText;
    return this;
  }
  
  public CRUDTest<Parent, Child> withPrependingNewObjects(boolean prepending) {
    this.prepending = prepending;
    return this;
  }
  
  public CRUDTest<Parent, Child> create(int count) {
    this.create = count;
    return this;
  }
  
  public CRUDTest<Parent, Child> changeText(int count, Provider textProvider) {
    this.changeText = count;
    this.textProvider = textProvider;
    return this;
  }
  
  public CRUDTest<Parent, Child> changeDate(int count, Provider dateProvider) {
    this.changeDate = count;
    this.dateProvider = dateProvider;
    return this;
  }
  
  public CRUDTest<Parent, Child> delete(int count) {
    this.delete = count;
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
  private Child getSomeObject() {
    ListOf<Child> children = parent.getChildren();
    return children.size() > 0 ? children.getSome() : parent.createChild();
  };
  
  @DataProvider(name = "createProvider")
  public Iterator<Object[]> defaultTextProvider() {
    List<Object[]> data = Lists.newArrayList();
    for (int i = 0; i < create; i++) {
      data.add(new Object[]{defaultText});
    }
    return data.iterator();
  }

  @DataProvider(name = "deleteProvider")
  public Iterator<Object[]> objectProvider() {
    int needMore = delete - parent.getChildren().size();
    for (int i = 0; i < needMore; i++) {
      parent.createChild();
    }
    ListOf<Child> children = parent.getChildren();
    List<Object[]> data = Lists.newArrayList();
    for (int i = 0; i < delete; i++) {
      Child selected = children.getSome();
      data.add(new Object[]{selected});
      children.remove(selected);
    }
    return data.iterator();
  }

  @DataProvider(name = "changeTextProvider")
  public Iterator<Object[]> objectAndStringProvider() {
    List<Object[]> data = Lists.newArrayList();
    for (int i = 0; i < changeText; i++) {
      data.add(new Object[]{getSomeObject(), textProvider.getData()});
    }
    return data.iterator();
  }

  @DataProvider(name = "changeDateProvider")
  public Iterator<Object[]> objectAndDataProvider() {
    List<Object[]> data = Lists.newArrayList();
    for (int i = 0; i < changeDate; i++) {
      data.add(new Object[]{getSomeObject(), dateProvider.getData()});
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
        equalTo(prepending
            ? objectsBefore.withPrepended(newObject)
            : objectsBefore.withAppended(newObject)));
  }

  @Test(enabled = true, dataProvider = "changeTextProvider")
  public void canChangeTextOf(Child objectToModify, String newText) {
    Child modifiedObject = objectToModify.changeTextTo(newText);

    assertThat(modifiedObject.text(),
        is(newText));
    assertThat(getAllObjects().without(modifiedObject),
        equalTo(objectsBefore.without(objectToModify)));
  }

  @Test(enabled = true, dataProvider = "deleteProvider")
  public void canDelete(Child objectToDelete) {
    objectToDelete.delete();

    assertThat(getAllObjects(),
        equalTo(objectsBefore.without(objectToDelete)));
  }

  @Test(enabled = true, dataProvider = "changeDateProvider")
  public void canChangeCreationDateOf(Child objectToModify, String newDate) {
    Child modifiedObject = objectToModify.changeCreationDateTo(newDate);

    assertThat(modifiedObject.creationDate(),
        is(newDate));
    assertThat(getAllObjects().without(modifiedObject),
        equalTo(objectsBefore.without(objectToModify)));
  }
}
