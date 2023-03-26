package TestBase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseClass {

    public static Properties properties;
    public static WebDriver driver;
    public static Date date = new Date();
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
    public static String dt = formatter.format(date);
    public static FileReader fileReader;
    protected final Logger logger = LogManager.getLogger(getClass());

    public static ExtentSparkReporter spark;
    public static ExtentTest test;
    public static ExtentReports extent;
    public static JsonFormatter json;
    public static String reportDestination = "target/reports/report_" + dt + ".html";
    public static final String JSON_ARCHIVE = "target/json/jsonArchive.json";

    public static void extentReportSpark() {

        spark = new ExtentSparkReporter(reportDestination);
        extent = new ExtentReports();
        extent.attachReporter(spark);
        json = new JsonFormatter(JSON_ARCHIVE);

        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Browser Name", properties.getProperty("BrowserName"));
        extent.setSystemInfo("Environment", properties.getProperty("Environment"));

        spark.config().setDocumentTitle("WebApp Automation Testing Report");
        spark.config().setReportName("WebApp Automation Test Suite");
        spark.config().setTimelineEnabled(Boolean.TRUE);
        spark.config().setOfflineMode(Boolean.TRUE);
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        spark.config().setTimelineEnabled(Boolean.TRUE);
    }
    public static void propertiesLoad() throws IOException {

        try {
            fileReader = new FileReader("src/test/resources/config.properties");
            Properties properties = new Properties();
            properties.load(fileReader);
            propertiesLoad(properties);

        } catch (FileNotFoundException ex) {
            test.info("*************************************************");
            test.info("Property file you are looking for does not exist.");
            test.info("*************************************************");
        }
    }

    public static void propertiesLoad(Properties config) throws IOException {

        try {
            fileReader = new FileReader("src/test/resources/config/"+config.getProperty("Environment")+"_web_config.properties");
            properties = new Properties();
            properties.load(fileReader);
            properties.setProperty("BrowserName",config.getProperty("BrowserName"));

        } catch (FileNotFoundException ex) {
            test.info("*************************************************");
            test.info("Property file you are looking for does not exist.");
            test.info("*************************************************");
        }
    }
    public static void autoOpenBrowser() {
        if (properties.getProperty("BrowserName").equalsIgnoreCase("chrome")) {

            WebDriverManager.chromedriver().setup();

            driver = new ChromeDriver();
            System.setProperty("webdriver.chrome.logfile", "./logs/chromeLogs.txt");
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        } else if (properties.getProperty("BrowserName").equalsIgnoreCase("firefox")) {

            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            System.setProperty("webdriver.firefox.logfile", "./logs/FirefoxLogs.txt");
            FirefoxOptions options = new FirefoxOptions();
//            options.setLogLevel(FirefoxDriverLogLevel.TRACE);
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        } else if (properties.getProperty("BrowserName").equalsIgnoreCase("safari")) {

            driver = new SafariDriver();
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        } else {

            WebDriverManager.chromiumdriver().setup();

            //   driver = new ChromiumDriver();
            System.setProperty("webdriver.chrome.logfile", "./logs/chromeLogs.txt");
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
    }
}
