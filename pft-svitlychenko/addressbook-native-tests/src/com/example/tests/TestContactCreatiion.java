package com.example.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.example.fw.Contact;

public class TestContactCreatiion extends TestBase{

	@Test
	public void shouldCreateContactWithValidData(){
		Contact contact = new Contact().setFirstName("tester").setLastName("tester");
		app.getContactHelper().createContact(contact);
		Contact createdContact  = app.getContactHelper().getFirstContact();
		Assert.assertEquals(contact, createdContact);
	}
}
