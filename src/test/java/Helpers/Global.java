package com.utility.library;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.aventstack.extentreports.ExtentTest;

public class Global {
	
	private Global(){
		
	}

	public static final String PROJECT_LOCATION = System.getProperty("user.dir")+ "\\";	
	public static String EXECUTION_RESULT_ZIP_FOLDER_NAME = "Report_";
	public static Sheet testSheet;
	public static boolean testResult = true;
	public static File filename;
	public static boolean continueExecutionFlag = true;
	public static BufferedWriter logTestCase ;
	public static boolean firstTestStepFlag = true;
	
	// Dictionary to maintain test result to print summary in email reports
	public static final Dictionary<String, String> testResultDetails = new Hashtable<String, String>();
	public static int testCounter = 0;
	public static String currentTestCaseName =null;

	// keep count of total test skip/pass/fail
	public static int totalSkipCount = 0;
	public static int totalPassCount = 0;
	public static int totalFailCount = 0;

	// Constants for platform instances 
	public static int totalWebPlatformTests=0;
	public static int totalAndroidPlatformTests=0;
	public static int totalAndroidBrowserPlatformTests =0;
	public static int totalIOSPlatformTests=0;
	public static int totalIOSBrowserPlatformTests=0;
	public static int totalAPIPlatformTests=0;

	// Dictionary maintaining the test cases count, start and end row for each platform type 
	public static final Dictionary<String, String> webPlatformTestCases = new Hashtable<String, String>();
	public static final Dictionary<String, String> androidPlatformTestCases = new Hashtable<String, String>();
	public static final Dictionary<String, String> androidBrowserPlatformTestCases = new Hashtable<String, String>();
	public static final Dictionary<String, String> iosPlatformTestCases = new Hashtable<String, String>();
	public static final Dictionary<String, String> iosBrowserPlatformTestCases = new Hashtable<String, String>();
	public static final Dictionary<String, String> apiPlatformTestCases = new Hashtable<String, String>();
	
	// Dictionary maintaining the ?????
	public static final Dictionary <String, String> platformTestBag = new Hashtable<String, String>();

	// Constants for the Platform Types
	public static final String WEB = "Web";
	public static final String ANDROID = "Android";
	public static final String IOS = "iOS";
	public static final String IOS_BROWSER = "iOSBrowser";
	public static final String API = "API";
	public static final String ANDROID_BROWSER = "AndroidBrowser";
	public static final String CHROME_BROWSER = "chrome";
	public static final String FIREFOX_BROWSER = "firefox";
	public static final String IE_BROWSER = "ie";
	public static final String SCREEN_SHOT_FOLDER = "extentReports\\testreport\\";

	// Constants for DB connectivity
	public static final String CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static final String DB_USERNAME = "db_username";
	public static final String DB_PASS = "db_password";
	public static final String DB_CONNECTION_URL = "db_connection_url";
	
	public static final String SEPARATOR_LINE = "------------------------------------------------------------------------------------------------------";
	public static final String PREFIX = "\"...";

	
	
	public static final String APPIUM_VERSION = "appium-version";
	public static final String PLATFORM_NAME ="platformName";
	public static final String PLATFORM_VERSION = "platformVersion";
	public static final String DEVICE_NAME = "deviceName";
	public static final String NEW_COMMAND_TIMEOUT = "newCommandTimeout";
	public static final String HUB_BASE_URL = "http://127.0.0.1:";
	public static final String HUB_BASE_URL_PREFIX = "/wd/hub";
	
	public static final String SKIP_MESSAGE = "Skipping the test as Is Enabled is NO";
	public static Dictionary<String, String> emailSummary =  new Hashtable<String, String>(); 
	
	public static final Dictionary <String, String> parametersAcrossActionsTestBag = new Hashtable<String, String>();
	public static final ArrayList <String>  TestCaseIDsFromTG = new  ArrayList<String>();  
	
	// EXTENT REPORT CONSTANTS
	public static Map<String, Object> featureList =  new HashMap<String, Object>(); 
	
	/******************PROJECT SPECIFIC CONSTANTS***********************/
	public static final String BN_TRANSACTION_TYPE = "IP";
	public static final String MO_TRANSACTION_TYPE = "MO";
	
	public static final String SALE_PENDING = "SALE_PENDING";
	
	public static final String ACTIVE_OFFERS_OPTION =  "Active Offers";
	public static final String ALL_OFFERS_OPTION =  "All Offers";
	public static final String DECLINED_OFFERS_OPTION =  "Declined Offers";
	public static final String CANCELED_OFFERS_OPTION =  "Canceled Offers";
	
	
	public static final String companyItemIdParam = ".companyitemid";
	public static final String COMPANY_ITEM_ID = "companyitemid";
	public static final String EVENT_TITLE = "eventTitle";
	public static final String transactionTypeParam = ".transactiontype";
	public static final String listPriceParam = ".listprice";
	public static final String totalSalePriceParam = ".totalsaleprice";
	public static final String buyerCounterAmountParam= ".buyercounteramount";
	public static final String buyerOfferAmountParam = ".buyerofferamount";
	public static final String percentageFeeParam = ".percentagefee";
	public static final String flatFeeParam = ".flatfee";
	public static final String minimumPriceParam = ".minimumprice";
	public static final String sellerCounterPriceParam = ".sellercounteramount";
	public static final String username = ".username";
	public static final String password = ".password";
	public static final String TEST_ID = "testId";
	public static final String BROWSE_BY_LIVE_AUCTIONS ="Live Auctions";
	public static final String BROWSE_BY_TODAYS_EVENTS ="Today's Events";
	public static final String BROWSE_BY_TIMED_EVENTS ="Timed Events";
	public static final String BROWSE_BY_BUYNOW_MAKEOFFER = "Buy Now / Make Offer";
	public static final String BROWSE_BY_ALL_EVENTS ="All Events";
	public static final String SEARCH_BROWSE_BY_COMPANIES = "Companies";
	public static final Dictionary <String, String> parametersAcrossTestBag = new Hashtable<String, String>();
	public static final String CREDITCARD_TYPE_PARAM = ".cctype";	
	public static final String BUYER_NAME = "newBuyerName";
	public static final String FEE_TYPE = ".feetype";
	public static final String SELLER_NAME = "sellername";
	public static final String ADMIN_NAME = "adminname";
	public static final String PARTNER_NAME = "partnername";
	
	/******************CORE - PROJECT SPECIFIC CONSTANTS***********************/
	public static final String CORE_USER_ROLE_PARAM = ".role";
	public static final String CORE_PARTNER_NAME_PARAM = ".partnername";
	public static final String CORE_ADMIN_NAME_PARAM = ".adminname";
	
	
}
