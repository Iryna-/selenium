package com.example.tests;

import static com.example.tests.GroupDataGenerator.loadGroupsFromXmlFile;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.example.utils.SortedListOf;


public class GroupModificationTests extends TestBase {
	
	@DataProvider	
	public Iterator<Object[]> groupsFromFile () throws IOException {
		return wrapGroupsForDataProvider(loadGroupsFromXmlFile(new File("groups.xml"))).iterator();
	}
	
	

	@Test(dataProvider = "groupsFromFile")
	public void modifySomeGroup(GroupData group) {
		
		
		
		//take a snapshot of the system before changes
		SortedListOf<GroupData> oldList = app.getModel().getGroups();
		
		Random rnd = new Random ();
		int index = rnd.nextInt(oldList.size()-1);
	
		
		//actions
		app.getGroupHelper().modifyGroup(index, group);
						
		//take a snapshot of the system after changes
		SortedListOf<GroupData> newList = app.getModel().getGroups();

		//compare snapshots
		assertThat(newList, equalTo (oldList.without(index).withAdded(group)));
		
		if (wantToCheck()){
			if ("yes".equals(app.getProperty("check_db"))){
				
					assertThat(app.getModel().getGroups(), equalTo (app.getHibernateHelper().listGroups()));	
				
			}
			if ("yes".equals(app.getProperty("check_ui"))){
				
			assertThat(app.getModel().getGroups(), equalTo (app.getGroupHelper().getUiGroups()));
		
		}
		}
		}
	}
