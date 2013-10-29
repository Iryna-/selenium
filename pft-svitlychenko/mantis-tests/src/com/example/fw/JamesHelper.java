package com.example.fw;

import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.net.telnet.TelnetClient;

public class JamesHelper extends HelperBase{

	private ApplicationManager app;

	private TelnetClient telnet = new TelnetClient();
	private InputStream in;
	private PrintStream out;

	public JamesHelper(ApplicationManager applicationManager) {
		super(applicationManager);
	}

	private void initTelnetSession() {
		String mailserver = app.getProperty("mailserver");
		int port = Integer.parseInt(app.getProperty("mailserver.adminport"));
		String login = app.getProperty("mailserver.adminlogin");
		String password = app.getProperty("mailserver.adminpassword");
		
		try {
			telnet.connect(mailserver, port);
			in = telnet.getInputStream();
			out = new PrintStream( telnet.getOutputStream() );

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Don't know why it doesn't allow login at the first attempt
		readUntil("Login id:");
		write("");
		readUntil("Password:");
		write("");

		// Second login attempt, must be successfull
		readUntil("Login id:");
		write(login);
		readUntil("Password:");
		write(password);

		// Read welcome message
		readUntil("Welcome "+login+". HELP for a list of commands");
	}
	
	private String readUntil(String pattern) {
		try {
			char lastChar = pattern.charAt(pattern.length() - 1);
			StringBuffer sb = new StringBuffer();
			char ch = (char) in.read();
			while (true) {
				System.out.print(ch);
				sb.append(ch);
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) {
						return sb.toString();
					}
				}
				ch = (char) in.read();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void write(String value) {
		try {
			out.println(value);
			out.flush();
			System.out.println(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void closeTelnetSession() {
		write("quit");
	}

	public boolean doesUserExist(String name) {
		initTelnetSession();
		write("verify " + name);
		String result = readUntil("exist");
		closeTelnetSession();
		return result.trim().equals("User " + name + " exist");
	}

	public void createUser(String name, String passwd) {
		initTelnetSession();
		write("adduser " + name + " " + passwd);
		String result = readUntil("User " + name + " added");
		closeTelnetSession();
	}

	public void deleteUser(String name) {
		initTelnetSession();
		write("deluser " + name);
		String result = readUntil("User " + name + " deleted");
		closeTelnetSession();
	}

}
