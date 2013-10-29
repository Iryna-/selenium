package ru.st.surrealtodo.tests;

import java.io.FileReader;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

import ru.st.surrealtodo.fw.Application;
import ru.st.surrealtodo.fw.TodoItem;
import ru.st.surrealtodo.fw.TodoList;
import ru.st.surrealtodo.fw.ListOf;
import ru.st.surrealtodo.fw.Page;
import ru.st.surrealtodo.fw.Tab;
import ru.st.testng.TracingListener;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

@Listeners({TracingListener.class})
public class TestBase {
	
	private static Logger log = LoggerFactory.getLogger(TestBase.class);
	
	static {
		SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
		StatusPrinter.print((LoggerContext) LoggerFactory.getILoggerFactory());
	}
	
	protected Application app;
	
	@BeforeTest
	@Parameters({"configFile"})
	public void setUp(@Optional String configFile) throws Exception {
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
		props.load(new FileReader(configFile));
		Application.getInstance().setProperties(props);
	}
	
	@BeforeClass
	public void getApplicationManager() {
		app = Application.getInstance();
	} 

	//@AfterTest
	public void tearDown() throws Exception {
		Application.getInstance().stop();
	}
	
  protected Tab getSomeTab() {
    ListOf<Tab> tabs = app.getTabs();
    return tabs.size() > 0 ? tabs.getSome() : app.createTab();
  }

  protected Page getSomePage() {
    return getSomePageOn(getSomeTab());
  }

  protected Page getSomePageOn(Tab tab) {
    ListOf<Page> pages = tab.getPages();
    return pages.size() > 0 ? pages.getSome() : tab.createPage();
  }

  protected TodoList getSomeList() {
    return getSomeListOn(getSomePage());
  }

  protected TodoList getSomeListOn(Page page) {
    ListOf<TodoList> lists = page.getLists();
    return lists.size() > 0 ? lists.getSome() : page.createList();
  }

  protected TodoItem getSomeItem() {
    return getSomeItemOn(getSomeList());
  }

  protected TodoItem getSomeItemOn(TodoList list) {
    ListOf<TodoItem> items = list.getChildren();
    return items.size() > 0 ? items.getSome() : list.createItem();
  }

  protected ListOf<Tab> getAtLeastTwoTabs() {
    ListOf<Tab> tabs = app.getTabs();
    if (tabs.size() < 2) {
      app.createTab();
      tabs = app.getTabs();
    }
    return tabs;
  }

  protected ListOf<Page> getAtLeastTwoPages(Tab tab) {
    ListOf<Page> pages = tab.getPages();
    if (pages.size() < 2) {
      tab.createPage();
      pages = tab.getPages();
    }
    return pages;
  }

  protected String randomNonEmptyString(int maxLen) {
    Random rnd = new Random();
    int len = rnd.nextInt(maxLen-1) + 1;
    String name = "";
    for (int i = 0; i < len; i++) {
      if (rnd.nextInt(10) == 0) {
        name += " ";
      } else {
        name += (char) (rnd.nextInt(94) + 32);
      }
    }
    return name.trim();
  }
}
