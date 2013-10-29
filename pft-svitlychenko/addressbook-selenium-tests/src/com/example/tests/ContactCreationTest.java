package com.example.tests;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.example.utils.SortedListOf;
import static com.example.tests.ContactDataGenerator.loadContactsFromXmlFile;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class ContactCreationTest extends TestBase {
	
	@DataProvider
	public Iterator<Object[]> contactsFromFile() throws IOException {
		return wrapContactsForDataProvider(loadContactsFromXmlFile(new File("contacts.xml"))).iterator();
	}
	
	
	@Test(dataProvider = "contactsFromFile")
	public void testContactCreationWithValidData(ContactData contact) throws Exception {
		
		//take a snapshot of the system before test
		
		SortedListOf<ContactData> oldList = app.getModel().getContacts();
		
		//actions
		app.getContactHelper().createContact(contact);
		
		//take a snapshot of the system after test
		
		SortedListOf<ContactData> newList = app.getModel().getContacts();
		
		//compare snapshots
	
		assertThat(newList, equalTo (oldList.withAdded(contact)));
		if (wantToCheck()){
		if ("yes".equals(app.getProperty("check_db"))){
		assertThat(app.getModel().getContacts(), equalTo (app.getHibernateHelper().listContacts()));
		}
		if ("yes".equals(app.getProperty("check_ui"))){
		assertThat(app.getModel().getContacts(), equalTo (app.getContactHelper().getUiContacts()));
	}
	}
	}
	}
