package com.example.tests;


import static org.junit.Assert.assertThat;
import java.util.Random;
import org.testng.annotations.Test;
import com.example.utils.SortedListOf;
import static org.hamcrest.Matchers.*;

public class GroupRemovalTests extends TestBase{

	
	@Test
	
	public void deleteSomeGroup () {
		
		
		//take a snapshot of the system before changes
		SortedListOf<GroupData> oldList = app.getModel().getGroups();
				
		Random rnd = new Random ();
		int index = rnd.nextInt(oldList.size()-1);
		
		//actions
				
		app.getGroupHelper().deleteGroup(index);
		
		
		//take a snapshot of the system after changes
		SortedListOf<GroupData> newList = app.getModel().getGroups();
		
		//compare snapshots
		assertThat(newList, equalTo(oldList.without(index)));		
		
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
