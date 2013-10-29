package com.example.fw;

import java.util.ArrayList;
import java.util.List;

public class Folders {

	private List<String> storedFolders = null;
	
	public Folders(List<String> folders) {
		
		this.storedFolders = new ArrayList<String>(folders);
		
	}

	public Folders withAdded(String folder) {
		Folders newList = new Folders(storedFolders);
		newList.storedFolders.add(folder);
		return newList;
	}

public Folders without(String folder) {
		Folders newList = new Folders(storedFolders);
		newList.storedFolders.remove(folder);
		return newList;
		}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((storedFolders == null) ? 0 : storedFolders.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Folders other = (Folders) obj;
		if (storedFolders == null) {
			if (other.storedFolders != null)
				return false;
		} else if (!storedFolders.equals(other.storedFolders))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Folders [" + storedFolders + "]";
	}

	
	
	
	
}
