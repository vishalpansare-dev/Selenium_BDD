package com.utility.library;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

public class LoggerUtil{
	InputStream input = null;
	static String timestamp;
	File logdir;
	static File logfile;
	File summarylogfile;
	static FileWriter fw;
	static FileWriter fwsummary;
	static BufferedWriter bw;
	static java.util.Date date;
	BufferedWriter logger = null;
	static BufferedWriter logsummary = null;
	static String logDashboard = "LogDashboard.log";
		
	//get Time stamp
	public static Timestamp getTimestamp(){
		date= new java.util.Date();
		return new Timestamp(date.getTime());
	}
	
	public static File getLogFile(){
		Global.filename =logfile;
		return logfile;
	}
	//create log folder and log file if doesn't exists 
	public static File getLogFile(String filename) throws IOException{
		File logfile;
		File logdir;
		
		logdir = new File("log");
		// if log directory doesn't exists, then create it
		if(!logdir.exists())
			logdir.mkdir();
		logfile = new File(logdir,filename);		
		// if log file doesn't exists, then create it
		if (!logfile.exists())
			logfile.createNewFile();
		
		return logfile;
	}
	
	//Get handle of summary log file "log/Dashboard.log" 
	public static BufferedWriter getDashboardLog() throws IOException{
		fwsummary = new FileWriter(getLogFile(logDashboard).getAbsolutePath());
		logsummary =  new BufferedWriter(fwsummary);
		return logsummary;
	}
	
	//Generate log file for current test batch
	public static BufferedWriter generateLogFile(String testsuite) throws IOException{
		 timestamp = getTimestamp().toString().replace(":", "_");
		    logfile = getLogFile(testsuite+ ".csv").getAbsoluteFile();
		    fw = new FileWriter(logfile);
			bw = new BufferedWriter(fw);
			return bw;
	}
	
	//Log INFO
	public static void logInfo(BufferedWriter bw, String info) throws IOException{	
		bw.write(getTimestamp()+"	INFO: "+info+"\n");
	}
	
	//Log ERROR
	public static void logError(BufferedWriter bw, String error) throws IOException{
		bw.write(getTimestamp()+"	ERROR: "+error+"\n");
	}
	
	//print decoration string in log file
	public static void print(BufferedWriter bw,String str) throws IOException{
		bw.write(str+"\n");
	}
	
	//Log TestCase
	public static void logTestSuite(String testCaseSuite) throws IOException{	
		Global.logTestCase.write("\n" + testCaseSuite);
	}
	//Log TestCase
	public static void logTestMetaData(String testCaseMetaData) throws IOException{	
		Global.logTestCase.append( "|"+ testCaseMetaData);
	}
	
	public static void logTestCaseStep(String testCaseStep) throws IOException {
	
		if(Global.firstTestStepFlag){
			logFirstTestCaseStep(testCaseStep);
		} else{
			 logNextTestCaseStep(testCaseStep); 
		}
	}
	//Log TestCase
	public static void logFirstTestCaseStep(String testCaseStep) throws IOException{	
		Global.logTestCase.write( "|"+ testCaseStep);
	}
	public static void logNextTestCaseStep(String testCaseStep) throws IOException{	
		Global.logTestCase.write("\n" + "|||||"+ testCaseStep);
	}
	
	//Log TestCase
	public static void logTestStepFirstAssertion (String testStepFirstVerification) throws IOException{	
			Global.logTestCase.append("|"+ testStepFirstVerification);
	}

	//Log TestCase
	public static void logTestStepNextAssertion (String testStepMultipleVerification) throws IOException{	
			Global.logTestCase.write("\n" + "||||||"+ testStepMultipleVerification);
	}

	public static void logSummary(String str) throws IOException{
		fwsummary = new FileWriter(getLogFile(logDashboard).getAbsolutePath(),true);
		logsummary = new BufferedWriter(fwsummary);
		logsummary.write(getTimestamp()+"	"+str+"\n");
		logsummary.close();
	}
	
	public static void printSummary(String str) throws IOException{
		fwsummary = new FileWriter(getLogFile(logDashboard).getAbsolutePath(),true);
		logsummary = new BufferedWriter(fwsummary);
		logsummary.append(str+"\n");
		logsummary.close();
	}
	
/*	public static void readTestCase() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("F:\\Sandbox\\Proxibid\\nbe-automation\\log\\TestCase.csv"));
		 
		  String st;
		  while ((st = br.readLine()) != null) {
		    System.out.println(st);
		  String strArray[] = st.split("\\|");
			System.out.println("String converted to String array");
		
			//print elements of String array
			for(int i=0; i < strArray.length; i++){
				System.out.println(strArray[i]);
			}
		  }
		  }*/
	
	
	
	}
	
