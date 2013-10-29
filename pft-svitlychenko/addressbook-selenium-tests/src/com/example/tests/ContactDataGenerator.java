package com.example.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.thoughtworks.xstream.XStream;

public class ContactDataGenerator {

	
	public static void main(String[] args) throws IOException {
		if (args.length < 3){
			System.out.println("Please specify parameters: <test data amount> <file> <format>");
			return;
		}
		int amount = Integer.parseInt(args[0]);
		File file = new File (args[1]);
		String format = args [2];
		
		if (file.exists()){
			System.out.println("File exists. Please remove it manually: " + file);
			return;
		}
		
		List<ContactData> contacts = generateRandomContacts(amount);
		if ("csv".equals(format)){
			saveContactsToCsvFile(contacts,file);
			}
		else if ("xml".equals(format)){
			saveContactsToXmlFile(contacts,file);
			}
		else{
			System.out.println("Unknown format" + format);
			return;
		}
	}

	private static void saveContactsToXmlFile(List<ContactData> contacts, File file) throws IOException {
		XStream xstream = new XStream();
		xstream.alias("contact", ContactData.class);
		String xml = xstream.toXML(contacts);
		FileWriter writer = new FileWriter(file);
		writer.write(xml);
		writer.close();
	}
	
    public static List<ContactData> loadContactsFromXmlFile(File file) {
    	XStream xstream = new XStream();
		xstream.alias("contact", ContactData.class);
		return (List<ContactData>) xstream.fromXML(file);
	}

	private static void saveContactsToCsvFile(List<ContactData> contacts, File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		for (ContactData contact : contacts) {
			writer.write(contact.getFirstname()+ "," + contact.getSurname() + 
					"," + contact.getAddress() + "," + contact.getHphone() + 
					"," + contact.getMphone() + "," + contact.getWphone() + 
					"," + contact.getEmail() +  "," + contact.getEmail2() + 
					"," + contact.getBday() + "," + contact.getBmonth() + 
					"," + contact.getByear() + "," + contact.getNew_group() + 
					"," + contact.getAddress2() +  "," + contact.getPhone2() + ",!" + "\n");
		}
		writer.close();
	}
	
	public static List<ContactData> loadContactsFromCsvFile(File file) throws IOException {
		List<ContactData> list = new ArrayList <ContactData>();
		FileReader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = bufferedReader.readLine();
		while (line != null){
			String[] part = line.split(",");
			ContactData group = new ContactData()
			.withFirstName(part[0])
			.withSurname(part[1])
			.withAddress(part[2])
			.withHPhone(part[3])
			.withMPhone(part[4])
			.withWPhone(part[5])
			.withEmail(part[6])
			.withEmail2(part[7])
			.withBDay(part[8])
			.withBMonth(part[9])
			.withBYear(part[10])
			.withNewGroup(part[11])
			.withAddress2(part[12])
			.withPhone2(part[13]);
			list.add(group);
			line = bufferedReader.readLine();
		}
		bufferedReader.close();
		return list;
	}


	public static List<ContactData> generateRandomContacts(int amount) {
		List<ContactData> list = new ArrayList <ContactData>();
		for (int i=0; i< amount; i++){
		
			ContactData contact = new ContactData()
			.withFirstName(generateRandomStringConact())
			.withSurname(generateRandomStringConact())
			.withAddress(generateRandomStringConact())
			.withHPhone(generateRandomStringPhone())
			.withMPhone(generateRandomStringPhone())
			.withWPhone(generateRandomStringPhone())
			.withEmail(generateRandomStringConact())
			.withEmail2(generateRandomStringConact())
			.withBDay(generateRandomStringBDay())
			.withBMonth("January")
			.withBYear(generateRandomStringBYear())
			.withNewGroup("[none]")
			.withAddress2(generateRandomStringConact())
			.withPhone2(generateRandomStringPhone());
			list.add(contact);
		}
		
		return list;
	}
	
	public static String generateRandomStringConact() {
		final String alpha = "abcdefghijklmnopqrstuvwxyz";
		final int N = alpha.length();
		String sletter = null;
		Random rnd = new Random();
		for (int i = 0; i < 50; i++) {
		     char   letter = alpha.charAt(rnd.nextInt(N));
		     sletter = Character.toString(letter);
		}
		
		if (rnd.nextInt(3) == 0){
			return "";
			}
			else {
				 String rString = "test " + sletter;
				      return rString;
			        }
				
	}
		
		public static String generateRandomStringBDay (){
			Random rnd = new Random();
			if (rnd.nextInt(3) == 0){
				return "-";
				}
				else {
					return Integer.toString(rnd.nextInt(29) + 1);
				}
		}
			public static String generateRandomStringBMonth (){
				Random rnd = new Random();
				if (rnd.nextInt(3) == 0){
					return "-";
					}
					else {
						return Integer.toString(rnd.nextInt(12) + 1);
					}
			}
				public static String generateRandomStringBYear (){
					Random rnd = new Random();
					if (rnd.nextInt(3) == 0){
						return "";
						}
						else {
							return Integer.toString(rnd.nextInt(114) + 1900);
						}
				}
					
				public static String generateRandomStringPhone (){
						Random rnd = new Random();
						if (rnd.nextInt(3) == 0){
							return "";
							}
							else {
								return Integer.toString(rnd.nextInt(10000000) + 2070000000);
							}
	}
}


