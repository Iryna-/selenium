package com.example.fw;

import java.util.ArrayList;
import java.util.List;

import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.operators.JTreeOperator;

public class FolderHelper extends HelperBase{

	public FolderHelper(ApplicationManager applicationManager) {
		super(applicationManager);
	}
	public Folders getFolders() {
		List<String> list = new ArrayList<String>();
		JTreeOperator tree = new JTreeOperator(mainFrame);
		Object[] children = tree.getChildren(tree.getRoot());
		for (Object child : children) {
			
			list.add("" + child);
		}
		return new Folders (list);
	}

	public String createFolder(String folderName) {
		manager.getMenuHelper().pushCreateFolder();
		JDialogOperator dialog = new JDialogOperator(mainFrame);
		new JTextFieldOperator(dialog).setText(folderName);
		new JButtonOperator(dialog, "OK").push();
		return waitMessageDialog("Warning", 3000);
	}
	
	
		public void removeFolder(String folderName){
			
			JTreeOperator existingTree = new JTreeOperator(mainFrame);
			int rowCount = existingTree.getRowCount();
			for(int i =0; i < rowCount; i++){
			String path = existingTree.getPathForRow(i).toString();
			if(path.equals("[jAddressBook, "+ folderName +"]")){
			existingTree.selectRow(i);
			manager.getMenuHelper().pushRemoveFolder();
			JDialogOperator dialog = new JDialogOperator("AddressBook: Confirm");
			new JButtonOperator(dialog, "Yes").push();
			//pushButtonInDialog("AddressBook: Confirm","Yes");
			return;
			}
			}
			throw new Error("Not folders to remove");
			}
		
	}


