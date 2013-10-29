package com.example.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.testng.annotations.Test;
import com.example.fw.Folders;

public class TestFolderRemoval extends TestBase{
	
	@Test
	public void testFolderRemoval(){
		String folder = "newfolder";
		Folders oldFolders = app.getFolderHelper().getFolders();
		if (oldFolders != null) {
		app.getFolderHelper().removeFolder(folder);
		Folders newFolders = app.getFolderHelper().getFolders();
		assertThat (newFolders, equalTo(oldFolders.without(folder)));
		}
		else {
			System.out.println("No folders to remove");
		}
	}
	
}
