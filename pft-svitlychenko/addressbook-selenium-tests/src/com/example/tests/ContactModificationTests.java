package com.example.tests;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.example.utils.SortedListOf;
import static com.example.fw.ContactHelper.MODIFICATION;
import static com.example.tests.ContactDataGenerator.loadContactsFromCsvFile;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ContactModificationTests extends TestBase {
	
	@DataProvider
	public Iterator<Object[]> contactsFromFile() throws IOException {
		return wrapContactsForDataProvider(loadContactsFromCsvFile(new File("contacts.txt"))).iterator();
	}
	
	
	@Test(dataProvider = "contactsFromFile")
		
	public void modifySomeContact (ContactData contact) {
		
		
		//take a snapshot of the system before test
		
		SortedListOf<ContactData> oldList = app.getModel().getContacts();
		
		Random rnd = new Random();
		int index = rnd.nextInt(oldList.size()-1);
		
		//actions
		app.getContactHelper().modifyContact( index, contact);
		
		//take a snapshot of the system after test
		
		SortedListOf<ContactData> newList = app.getModel().getContacts();
						
		//compare snapshots
		assertThat(newList, equalTo (oldList.without(index).withAdded(contact)));
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