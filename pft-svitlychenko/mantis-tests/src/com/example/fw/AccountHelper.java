package com.example.fw;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class AccountHelper extends WebDriverHelperBase {

	public AccountHelper(ApplicationManager applicationManager) {
		super (applicationManager);
	}

	public void signUp(User user) {
		openUrl ("/");
		click(By.cssSelector("span.bracket-link"));
		type(By.name("username"), user.username);
		type(By.name("email"), user.email);
		click(By.cssSelector("input.button"));
		
		WebElement errorMessage = findElement(By.cssSelector("table.width50 tbody tr td p"));
		if(errorMessage != null){
			throw new RuntimeException (errorMessage.getText());
		}
		
		pause(3000);
		
		String msg = manager.getMailHelper().getNewMail(user.username, user.password);
		String confiramtionLink = getConfirmationLink(msg);
		openAbsoluteUrl (confiramtionLink);
		
		type(By.name("password"), user.password);
		type(By.name("password_confirm"), user.password);
		click(By.cssSelector("input.button"));
	}

	

	

	public String getConfirmationLink(String text) {
		Pattern regex = Pattern.compile("http\\S*");
		Matcher matcher = regex.matcher(text);
		if (matcher.find()) {
			return matcher.group();
		} else {
			return "";
		}
	}

	public void login(User user) {
		openUrl ("/");
		type(By.name("username"), user.username);
		type(By.name("password"), user.password);
		click(By.cssSelector("input.button"));
	}
	
	public String loggedInUser() {
		WebElement element = findElement(By.cssSelector("td.login-info-left span"));
		return element.getText();
	}
}

