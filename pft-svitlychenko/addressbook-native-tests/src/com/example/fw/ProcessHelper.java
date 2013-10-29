package com.example.fw;

import java.io.IOException;

public class ProcessHelper extends HelperBase{
	
	private Process process;
	public ProcessHelper(ApplicationManager applicationManager){
		super (applicationManager);
	}
 public void startAUT() throws IOException{
	 String command = manager.getProperty("app.path");
	process = Runtime.getRuntime().exec(command);
	 
 }
public void stopAUT(){
	 process.destroy();
 }

}
