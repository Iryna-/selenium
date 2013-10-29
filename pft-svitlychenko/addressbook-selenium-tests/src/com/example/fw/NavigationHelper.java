package com.example.fw;

import org.openqa.selenium.By;

public class NavigationHelper extends WebDriverHelperBase {

	public NavigationHelper(ApplicationManager manager) {
		super(manager);
	}

	public NavigationHelper mainPage() {
		if(! onMainPage()){
			
			click(By.linkText("home"));
		}
		
		return this;
	}

	
	public NavigationHelper groupsPage() {
		if (! onGroupsPage()){
			
			click(By.linkText("groups"));
			}
		
		return this;
	}
//-----------------------------------------------------------------------------
	private boolean onGroupsPage() {
			if (driver.getCurrentUrl().contains("/group.php") && driver.findElements(By.name("new")).size() > 0) {
			return true;
		} 
		else {
			return false;
		}
		}



	private boolean onMainPage() {
		return driver.findElements(By.id("maintable")).size() > 0 ;
	}
}
