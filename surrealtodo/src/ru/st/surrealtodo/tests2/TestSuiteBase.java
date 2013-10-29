package ru.st.surrealtodo.tests2;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

import ru.st.surrealtodo.fw.AppObject;
import ru.st.surrealtodo.fw.Application;
import ru.st.surrealtodo.fw.ListOf;
import ru.st.surrealtodo.fw.Page;
import ru.st.surrealtodo.fw.Tab;
import ru.st.surrealtodo.fw.TabHelper;
import ru.st.surrealtodo.fw.TodoItem;
import ru.st.surrealtodo.fw.TodoList;
import ru.st.testng.TracingListener;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

@Listeners({TracingListener.class})
public class TestSuiteBase {

  private static Logger log = LoggerFactory.getLogger(TestSuiteBase.class);
  
  static {
    SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
    StatusPrinter.print((LoggerContext) LoggerFactory.getILoggerFactory());
  }

  protected Application app;

  public TestSuiteBase() {
    this(null);
  }

  @Parameters({"configFile"})
  public TestSuiteBase(@Optional String configFile) {
    if (configFile == null) {
      configFile = System.getProperty("configFile");
    }
    if (configFile == null) {
      configFile = System.getenv("configFile");
    }
    if (configFile == null) {
      configFile = "application.properties";
    }
    log.debug("Init ApplicationManager from file {}", configFile);
    Properties props = new Properties();
    try {
      props.load(new FileReader(configFile));
    } catch (IOException e) {
      e.printStackTrace();
    }
    app = Application.getInstance();
    app.setProperties(props);
  }
  
  //@AfterTest
  public void tearDown() throws Exception {
    Application.getInstance().stop();
  }

  protected <Parent extends AppObject<Parent,Child>, Child extends AppObject<Child,?>> Child getSomeChildOf(Parent parent) {
    ListOf<Child> children = parent.getChildren();
    return children.size() > 0 ? children.getSome() : parent.createChild();
  }

  protected Tab getSomeTab() {
    return getSomeChildOf(app.topLevel());
  }

  protected Page getSomePage() {
    return getSomeChildOf(getSomeTab());
  }

  protected TodoList getSomeList() {
    return getSomeChildOf(getSomePage());
  }

  protected TodoItem getSomeItem() {
    return getSomeChildOf(getSomeList());
  }

  protected class TopLevelProvider implements ParentProvider<TabHelper> {
    public TabHelper getObject() {
      return app.topLevel();
    }
    public String newObjectDefaultText() {
      return "New Tab";
    }
    public boolean newObjectPrepending() {
      return false;
    }
  }

  protected class TabProvider implements ParentProvider<Tab> {
    public Tab getObject() {
      return getSomeTab();
    }
    public String newObjectDefaultText() {
      return "New Page";
    }
    public boolean newObjectPrepending() {
      return false;
    }
  }

  protected class PageProvider implements ParentProvider<Page> {
    public Page getObject() {
      return getSomePage();
    }
    public String newObjectDefaultText() {
      return "New List";
    }
    public boolean newObjectPrepending() {
      return true;
    }
  }

  protected class ListProvider implements ParentProvider<TodoList> {
    public TodoList getObject() {
      return getSomeList();
    }
    public String newObjectDefaultText() {
      return "Double-click to edit";
    }
    public boolean newObjectPrepending() {
      return false;
    }
  }
}
