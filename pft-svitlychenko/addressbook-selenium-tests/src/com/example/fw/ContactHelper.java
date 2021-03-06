package com.example.fw;

import static com.example.fw.ContactHelper.CREATION;
import static com.example.fw.ContactHelper.MODIFICATION;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.example.tests.ContactData;
import com.example.utils.SortedListOf;


public class ContactHelper extends WebDriverHelperBase {

	public static boolean CREATION = true;
	public static boolean MODIFICATION = false;
	
	public ContactHelper(ApplicationManager manager) {
		super(manager);
	}

	public SortedListOf<ContactData> getUiContacts() {
        
		SortedListOf<ContactData> contacts = new SortedListOf<ContactData>();
		manager.navigateTo().mainPage();
		List<WebElement> rows = driver.findElements(By.name("entry"));
		for (WebElement row : rows) {
       
       
        List <WebElement> cells = row.findElements(By.tagName("td"));
    String lastname = cells.get(1).getText();
    String surname = lastname;
    String firstname = cells.get(2).getText();
    String firstName = firstname;
    String email = cells.get(3).getText();
    String Email = email;
    String phone = cells.get(4).getText();
    String hphone = phone;
    ContactData contact = new ContactData().withSurname(surname);
    contact = contact.withFirstName(firstName);
    contact = contact.withEmail(Email);
    contact = contact.withHPhone(hphone);
    contacts.add(contact);
}
            return contacts;   
}


	public ContactHelper createContact(ContactData contact) {
		manager.navigateTo().mainPage();
		initContactCreation();
		fillInContactForm(contact, CREATION);
		submitForm();
		returnToMainPage();
		manager.getModel().addContact(contact);
		return this;
	}

	public ContactHelper modifyContact(int index, ContactData contact) {
		manager.navigateTo().mainPage();
		initContactModification(index);
		fillInContactForm(contact, MODIFICATION);
		submitContactModification();
		returnToMainPage();
		manager.getModel().removeContact(index).addContact(contact);
		return this;
	}

	public ContactHelper deleteContact(int index) {
		manager.navigateTo().mainPage();
		initContactModification(index);
		submitContactRemoval();
		returnToMainPage();
		manager.getModel().removeContact(index);
		return this;
	}

	
//--------------------------------------------------------------------------------------------------
	
	public ContactHelper initContactCreation() {
		click(By.linkText("add new"));
		return this;
	}

	public ContactHelper fillInContactForm(ContactData contact, boolean formType) {
		type(By.name("firstname"), contact.getFirstname());
		type(By.name("lastname"), contact.getSurname());
		type(By.name("address"), contact.getAddress());
		type(By.name("home"), contact.getHphone());
		type(By.name("mobile"), contact.getMphone());
		type(By.name("work"), contact.getWphone());
		type(By.name("email"), contact.getEmail());
		type(By.name("email2"), contact.getEmail2());
		selectByText(By.name("bday"), contact.getBday());
		selectByText(By.name("bmonth"), contact.getBmonth());
		type(By.name("byear"), contact.getByear());
		if (formType == CREATION){
		//selectByText(By.name("new_group"), "group 1");
		}
		else {
			if (driver.findElements(By.name("new_group")).size() != 0){
				throw new Error ("Group selector exists in contact modification form");
			}
		}
			
		type(By.name("address2"), contact.getAddress2());
		type(By.name("phone2"), contact.getPhone2());
		return this;
	}
	
		
	public ContactHelper initContactModification(int index) {
		click(By.xpath(".//*[@id='maintable']/tbody/tr[" + (index+2) + "]/td[7]/a/img"));
		return this;
	}

	public ContactHelper submitForm() {
		click(By.name("submit"));
		return this;
	}	
		
	public ContactHelper submitContactModification() {
		click(By.xpath(".//*[@value='Update']"));
		return this;
	}

	public ContactHelper returnToMainPage() {
		click(By.linkText("home page"));
		return this;
	}

	public ContactHelper submitContactRemoval() {
		click(By.xpath(".//*[@value='Delete']"));
		return this;
		}

	
	

}
