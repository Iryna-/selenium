package com.example.tests;

import java.util.Random;
import org.testng.annotations.Test;
import com.example.utils.SortedListOf;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ContactRemovalTests extends TestBase{

	@Test
	
	public void removeSomeContact () {
		
		//take a snapshot of the system before test
		SortedListOf<ContactData> oldList = app.getModel().getContacts();
		
		Random rnd = new Random();
		int index = rnd.nextInt(oldList.size()-1);
						
		//actions
		app.getContactHelper().deleteContact(index);
		
		
		//take a snapshot of the system after test
		
		SortedListOf<ContactData> newList = app.getModel().getContacts();
						
		//compare snapshots
		assertThat(newList, equalTo(oldList.without(index)));			
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