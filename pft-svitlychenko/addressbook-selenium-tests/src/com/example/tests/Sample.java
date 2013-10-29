package com.example.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import com.example.fw.ApplicationManager;


public class Sample {

	public static void main (String[] args) throws FileNotFoundException, IOException{
		Properties properties = new Properties();
		properties.load(new FileReader (new File("application.properties")));
		ApplicationManager app = new ApplicationManager (properties);
		System.out.println(app.getHibernateHelper().listGroups());
	}
}
