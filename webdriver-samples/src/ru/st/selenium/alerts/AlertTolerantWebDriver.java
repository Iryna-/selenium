package ru.st.selenium.alerts;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.logging.Logs;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlertTolerantWebDriver implements WebDriver, JavascriptExecutor,
    TakesScreenshot, WrapsDriver, HasInputDevices, HasTouchScreen {

  private final WebDriver driver;

  private void logUnhandledAlert(UnhandledAlertException target) {
    System.err.println("Unhandled alert: " + target.getAlertText());
    target.printStackTrace();
  }

  public AlertTolerantWebDriver(final WebDriver driver) {
    Class<?>[] allInterfaces = extractInterfaces(driver);

    this.driver = (WebDriver) Proxy.newProxyInstance(
        AlertTolerantWebDriver.class.getClassLoader(),
        allInterfaces,
        new InvocationHandler() {
          @Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String m = method.getName();
            if ("getWrappedDriver".equals(m)) {
              return driver;
            }

            try {
              Object result = method.invoke(driver, args);
              return result;
            } catch (InvocationTargetException e) {
              Throwable target = e.getTargetException();
              if (target instanceof UnhandledAlertException) {
                logUnhandledAlert((UnhandledAlertException) target);
                // try again
                try {
                  Object result = method.invoke(driver, args);
                  return result;
                } catch (InvocationTargetException e1) {
                  throw e1.getTargetException();
                }
              }
              throw target;
            }
          }
        });
  }

  private Class<?>[] extractInterfaces(Object object) {
    Set<Class<?>> allInterfaces = new HashSet<Class<?>>();
    allInterfaces.add(WrapsDriver.class);
    if (object instanceof WebElement) {
      allInterfaces.add(WrapsElement.class);
    }
    extractInterfaces(allInterfaces, object.getClass());

    return allInterfaces.toArray(new Class<?>[allInterfaces.size()]);
  }

  private void extractInterfaces(Set<Class<?>> addTo, Class<?> clazz) {
    if (Object.class.equals(clazz)) {
      return; // Done
    }

    Class<?>[] classes = clazz.getInterfaces();
    addTo.addAll(Arrays.asList(classes));
    extractInterfaces(addTo, clazz.getSuperclass());
  }


  @Override
public WebDriver getWrappedDriver() {
    return driver;
  }

  @Override
public void get(String url) {
    driver.get(url);
  }

  @Override
public String getCurrentUrl() {
    return driver.getCurrentUrl();
  }

  @Override
public String getTitle() {
    return driver.getTitle();
  }

  @Override
public List<WebElement> findElements(By by) {
    List<WebElement> temp = driver.findElements(by);
    List<WebElement> result = new ArrayList<WebElement>(temp.size());
    for (WebElement element : temp) {
      result.add(createWebElement(element));
    }
    return result;
  }

  @Override
public WebElement findElement(By by) {
    return createWebElement(driver.findElement(by));
  }

  @Override
public String getPageSource() {
    return driver.getPageSource();
  }

  @Override
public void close() {
    driver.close();
  }

  @Override
public void quit() {
    driver.quit();
  }

  @Override
public Set<String> getWindowHandles() {
    return driver.getWindowHandles();
  }

  @Override
public String getWindowHandle() {
    return driver.getWindowHandle();
  }

  @Override
public Object executeScript(String script, Object... args) {
    if (driver instanceof JavascriptExecutor) {
      Object[] usedArgs = unpackWrappedArgs(args);
      return ((JavascriptExecutor) driver).executeScript(script, usedArgs);
    }

    throw new UnsupportedOperationException(
        "Underlying driver instance does not support executing javascript");
  }

  @Override
public Object executeAsyncScript(String script, Object... args) {
    if (driver instanceof JavascriptExecutor) {
      Object[] usedArgs = unpackWrappedArgs(args);
      return ((JavascriptExecutor) driver).executeAsyncScript(script, usedArgs);
    }

    throw new UnsupportedOperationException(
        "Underlying driver instance does not support executing javascript");
  }

  private Object[] unpackWrappedArgs(Object... args) {
    // Walk the args: the various drivers expect unpacked versions of the
    // elements
    Object[] usedArgs = new Object[args.length];
    for (int i = 0; i < args.length; i++) {
      usedArgs[i] = unpackWrappedElement(args[i]);
    }
    return usedArgs;
  }

  private Object unpackWrappedElement(Object arg) {
    if (arg instanceof List<?>) {
      List<Object> aList = (List<Object>) arg;
      List<Object> toReturn = new ArrayList<Object>();
      for (int j = 0; j < aList.size(); j++) {
        toReturn.add(unpackWrappedElement(aList.get(j)));
      }
      return toReturn;
    } else if (arg instanceof Map<?, ?>) {
      Map<Object, Object> aMap = (Map<Object, Object>) arg;
      Map<Object, Object> toReturn = new HashMap<Object, Object>();
      for (Object key : aMap.keySet()) {
        toReturn.put(key, unpackWrappedElement(aMap.get(key)));
      }
      return toReturn;
    } else if (arg instanceof AlertTolerantWebElement) {
      return ((AlertTolerantWebElement) arg).getWrappedElement();
    } else {
      return arg;
    }
  }

  @Override
public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
    if (driver instanceof TakesScreenshot) {
      return ((TakesScreenshot) driver).getScreenshotAs(target);
    }

    throw new UnsupportedOperationException(
        "Underlying driver instance does not support taking screenshots");
  }

  @Override
public TargetLocator switchTo() {
    return new AlertTolerantTargetLocator(driver.switchTo());
  }

  @Override
public Navigation navigate() {
    return new TracingNavigation(driver.navigate());
  }

  @Override
public Options manage() {
    return new TracingOptions(driver.manage());
  }

  private WebElement createWebElement(WebElement from) {
    return new AlertTolerantWebElement(from);
  }

  @Override
public Keyboard getKeyboard() {
    if (driver instanceof HasInputDevices) {
      return new AlertTolerantKeyboard(driver);
    } else {
      throw new UnsupportedOperationException(
          "Underlying driver does not implement advanced user interactions.");
    }
  }

  @Override
public Mouse getMouse() {
    if (driver instanceof HasInputDevices) {
      return new AlertTolerantMouse(driver);
    } else {
      throw new UnsupportedOperationException(
          "Underlying driver does not implement advanced user interactions.");
    }
  }

  @Override
public TouchScreen getTouch() {
    if (driver instanceof HasTouchScreen) {
      return new AlertTolerantTouch(driver);
    } else {
      throw new UnsupportedOperationException(
          "Underlying driver does not implement touch api.");
    }
  }

  private class AlertTolerantWebElement implements WebElement, WrapsElement, WrapsDriver, Locatable {

    private final WebElement element;
    private final WebElement underlyingElement;

    private AlertTolerantWebElement(final WebElement element) {
      this.element = (WebElement) Proxy.newProxyInstance(
          AlertTolerantWebDriver.class.getClassLoader(),
          extractInterfaces(element),
          new InvocationHandler() {
            @Override
			public Object invoke(Object proxy, Method method,
                Object[] args) throws Throwable {
              String m = method.getName();
              if ("getWrappedElement".equals(m)) {
                return element;
              }

              try {
                Object result = method.invoke(element, args);
                return result;
              } catch (InvocationTargetException e) {
                Throwable target = e.getTargetException();
                if (target instanceof UnhandledAlertException) {
                  logUnhandledAlert((UnhandledAlertException) target);
                  // try again
                  try {
                    Object result = method.invoke(element, args);
                    return result;
                  } catch (InvocationTargetException e1) {
                    throw e1.getTargetException();
                  }
                }
                throw target;
              }
            }
          });
      this.underlyingElement = element;
    }

    @Override
	public void click() {
      element.click();
    }

    @Override
	public void submit() {
      element.submit();
    }

    @Override
	public void sendKeys(CharSequence... keysToSend) {
      element.sendKeys(keysToSend);
    }

    @Override
	public void clear() {
      element.clear();
    }

    @Override
	public String getTagName() {
      return element.getTagName();
    }

    @Override
	public String getAttribute(String name) {
      return element.getAttribute(name);
    }

    @Override
	public boolean isSelected() {
      return element.isSelected();
    }

    @Override
	public boolean isEnabled() {
      return element.isEnabled();
    }

    @Override
	public String getText() {
      return element.getText();
    }

    @Override
	public boolean isDisplayed() {
      return element.isDisplayed();
    }

    @Override
	public Point getLocation() {
      return element.getLocation();
    }

    @Override
	public Dimension getSize() {
      return element.getSize();
    }

    @Override
	public String getCssValue(String propertyName) {
      return element.getCssValue(propertyName);
    }

    @Override
	public WebElement findElement(By by) {
      return createWebElement(element.findElement(by));
    }

    @Override
	public List<WebElement> findElements(By by) {
      List<WebElement> temp = element.findElements(by);
      List<WebElement> result = new ArrayList<WebElement>(temp.size());
      for (WebElement element : temp) {
        result.add(createWebElement(element));
      }
      return result;
    }

    @Override
	public WebElement getWrappedElement() {
      return underlyingElement;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof WebElement)) {
        return false;
      }

      WebElement other = (WebElement) obj;
      if (other instanceof WrapsElement) {
        other = ((WrapsElement) other).getWrappedElement();
      }

      return underlyingElement.equals(other);
    }

    @Override
    public int hashCode() {
      return underlyingElement.hashCode();
    }

    @Override
	public WebDriver getWrappedDriver() {
      return driver;
    }

    @Override
	public Coordinates getCoordinates() {
      Coordinates coordinates = ((Locatable) underlyingElement).getCoordinates();
      return coordinates;
    }
  }

  private class TracingNavigation implements Navigation {

    private final WebDriver.Navigation navigation;

    TracingNavigation(Navigation navigation) {
      this.navigation = navigation;
    }

    @Override
	public void to(String url) {
      navigation.to(url);
    }

    @Override
	public void to(URL url) {
      to(String.valueOf(url));
    }

    @Override
	public void back() {
      navigation.back();
    }

    @Override
	public void forward() {
      navigation.forward();
    }

    @Override
	public void refresh() {
      navigation.refresh();
    }
  }

  private class TracingOptions implements Options {

    private Options options;

    private TracingOptions(Options options) {
      this.options = options;
    }

    @Override
	public void addCookie(Cookie cookie) {
      options.addCookie(cookie);
    }

    @Override
	public void deleteCookieNamed(String name) {
      options.deleteCookieNamed(name);
    }

    @Override
	public void deleteCookie(Cookie cookie) {
      options.deleteCookie(cookie);
    }

    @Override
	public void deleteAllCookies() {
      options.deleteAllCookies();
    }

    @Override
	public Set<Cookie> getCookies() {
      return options.getCookies();
    }

    @Override
	public Cookie getCookieNamed(String name) {
      return options.getCookieNamed(name);
    }

    @Override
	public Timeouts timeouts() {
      return options.timeouts();
    }

    @Override
	public ImeHandler ime() {
      return options.ime();
    }

    @Override
    public Logs logs() {
      return options.logs();
    }

    @Override
    public Window window() {
      return options.window();
    }
  }

  private class AlertTolerantTargetLocator implements TargetLocator {

    private final TargetLocator targetLocator;

    private AlertTolerantTargetLocator(final TargetLocator targetLocator) {
      this.targetLocator = (TargetLocator) Proxy.newProxyInstance(
          AlertTolerantWebDriver.class.getClassLoader(),
          extractInterfaces(targetLocator),
          new InvocationHandler() {
            @Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
              try {
                Object result = method.invoke(targetLocator, args);
                return result;
              } catch (InvocationTargetException e) {
                Throwable target = e.getTargetException();
                if (target instanceof UnhandledAlertException) {
                  logUnhandledAlert((UnhandledAlertException) target);
                  // try again
                  try {
                    Object result = method.invoke(targetLocator, args);
                    return result;
                  } catch (InvocationTargetException e1) {
                    throw e1.getTargetException();
                  }
                }
                throw target;
              }
            }

          });
    }

    @Override
	public WebDriver frame(int frameIndex) {
      return targetLocator.frame(frameIndex);
    }

    @Override
	public WebDriver frame(String frameName) {
      return targetLocator.frame(frameName);
    }

    @Override
	public WebDriver frame(WebElement frameElement) {
      return targetLocator.frame(frameElement);
    }

    @Override
	public WebDriver window(String windowName) {
      return targetLocator.window(windowName);
    }

    @Override
	public WebDriver defaultContent() {
      return targetLocator.defaultContent();
    }

    @Override
	public WebElement activeElement() {
      return targetLocator.activeElement();
    }

    @Override
	public Alert alert() {
      return targetLocator.alert();
    }
  }

  public class AlertTolerantKeyboard implements Keyboard {

    private final WebDriver driver;
    private final Keyboard keyboard;

    public AlertTolerantKeyboard(WebDriver driver) {
      this.driver = driver;
      final Keyboard kb = ((HasInputDevices) this.driver).getKeyboard();
      this.keyboard = (Keyboard) Proxy.newProxyInstance(
          AlertTolerantWebDriver.class.getClassLoader(),
          extractInterfaces(kb),
          new InvocationHandler() {
            @Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
              try {
                Object result = method.invoke(kb, args);
                return result;
              } catch (InvocationTargetException e) {
                Throwable target = e.getTargetException();
                if (target instanceof UnhandledAlertException) {
                  logUnhandledAlert((UnhandledAlertException) target);
                  // try again
                  try {
                    Object result = method.invoke(kb, args);
                    return result;
                  } catch (InvocationTargetException e1) {
                    throw e1.getTargetException();
                  }
                }
                throw target;
              }
            }
          });
    }

    @Override
	public void sendKeys(CharSequence... keysToSend) {
      keyboard.sendKeys(keysToSend);
    }

    @Override
	public void pressKey(CharSequence keyToPress) {
      keyboard.pressKey(keyToPress);
    }

    @Override
	public void releaseKey(CharSequence keyToRelease) {
      keyboard.releaseKey(keyToRelease);
    }

  }

  public class AlertTolerantMouse implements Mouse {
    private final WebDriver driver;
    private final Mouse mouse;

    public AlertTolerantMouse(WebDriver driver) {
      this.driver = driver;
      final Mouse ms = ((HasInputDevices) this.driver).getMouse();
      this.mouse = (Mouse) Proxy.newProxyInstance(
          AlertTolerantWebDriver.class.getClassLoader(),
          extractInterfaces(ms),
          new InvocationHandler() {
            @Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
              try {
                Object result = method.invoke(ms, args);
                return result;
              } catch (InvocationTargetException e) {
                Throwable target = e.getTargetException();
                if (target instanceof UnhandledAlertException) {
                  logUnhandledAlert((UnhandledAlertException) target);
                  // try again
                  try {
                    Object result = method.invoke(ms, args);
                    return result;
                  } catch (InvocationTargetException e1) {
                    throw e1.getTargetException();
                  }
                }
                throw target;
              }
            }
          });
    }

    @Override
	public void click(Coordinates where) {
      mouse.click(where);
    }

    @Override
	public void doubleClick(Coordinates where) {
      mouse.doubleClick(where);
    }

    @Override
	public void mouseDown(Coordinates where) {
      mouse.mouseDown(where);
    }

    @Override
	public void mouseUp(Coordinates where) {
      mouse.mouseUp(where);
    }

    @Override
	public void mouseMove(Coordinates where) {
      mouse.mouseMove(where);
    }

    @Override
	public void mouseMove(Coordinates where, long xOffset, long yOffset) {
      mouse.mouseMove(where, xOffset, yOffset);
    }

    @Override
	public void contextClick(Coordinates where) {
      mouse.contextClick(where);
    }
  }

  public class AlertTolerantTouch implements TouchScreen {

    private final WebDriver driver;
    private final TouchScreen touchScreen;

    public AlertTolerantTouch(WebDriver driver) {
      this.driver = driver;
      final TouchScreen ts = ((HasTouchScreen) this.driver).getTouch();
      this.touchScreen = (TouchScreen) Proxy.newProxyInstance(
          AlertTolerantWebDriver.class.getClassLoader(),
          extractInterfaces(ts),
          new InvocationHandler() {
            @Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
              try {
                Object result = method.invoke(ts, args);
                return result;
              } catch (InvocationTargetException e) {
                Throwable target = e.getTargetException();
                if (target instanceof UnhandledAlertException) {
                  logUnhandledAlert((UnhandledAlertException) target);
                  // try again
                  try {
                    Object result = method.invoke(ts, args);
                    return result;
                  } catch (InvocationTargetException e1) {
                    throw e1.getTargetException();
                  }
                }
                throw target;
              }
            }
          });
    }

    @Override
	public void singleTap(Coordinates where) {
      touchScreen.singleTap(where);
    }

    @Override
	public void down(int x, int y) {
      touchScreen.down(x, y);
    }

    @Override
	public void up(int x, int y) {
      touchScreen.up(x, y);
    }

    @Override
	public void move(int x, int y) {
      touchScreen.move(x, y);
    }

    @Override
	public void scroll(Coordinates where, int xOffset, int yOffset) {
      touchScreen.scroll(where, xOffset, yOffset);
    }

    @Override
	public void doubleTap(Coordinates where) {
      touchScreen.doubleTap(where);
    }

    @Override
	public void longPress(Coordinates where) {
      touchScreen.longPress(where);
    }

    @Override
	public void scroll(int xOffset, int yOffset) {
      touchScreen.scroll(xOffset, yOffset);
    }

    @Override
	public void flick(int xSpeed, int ySpeed) {
      touchScreen.flick(xSpeed, ySpeed);
    }

    @Override
	public void flick(Coordinates where, int xOffset, int yOffset, int speed) {
      touchScreen.flick(where, xOffset, yOffset, speed);
    }
  }
}
