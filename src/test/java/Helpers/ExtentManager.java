package com.utility.library;
import java.io.File;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class ExtentManager {
	
	private ExtentManager(){
		
	}
	
	private static ExtentReports extent;
	/**
	 * Get instance of Extent Report
	 * @return extent instance
	 */
	public static ExtentReports getInstance() {

		if (extent == null) {
			
			//Date d = new Date();
			//String filename = d.toString().replace(":", "_").replace(" ","_")+".html";
			
			String filename = "ExecutionSummary.html";
			String testReportFolder = Global.PROJECT_LOCATION +"extentReports//testreport";
			ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(testReportFolder+ "//"+filename);
			htmlReporter.loadXMLConfig(new File(Global.PROJECT_LOCATION + "extentReports//ReportsConfig.xml"));
			 extent = new ExtentReports ();
			 extent.attachReporter(htmlReporter);
			 extent.setSystemInfo("Host Name", "NextVote");
			 extent.setSystemInfo("Environment", "QA-1.0.84.43-12747");
			 extent.setSystemInfo("User Name", "Vikas K");
			// extent.setSystemInfo("Version", "1.0.81.63-12122"); 
			 
		}
		
		return extent;
	}
	


	
	
}