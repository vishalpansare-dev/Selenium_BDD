package StepDef;

import TestBase.BaseClass;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.cucumber.java.*;
import org.testng.annotations.AfterSuite;

import java.io.IOException;


public class Hooks extends BaseClass {

    @BeforeAll
    public static void beforeAll() throws IOException {
        System.out.println("Before suite...");
//        Loadproperty()
        propertiesLoad();
//        StartReporting()
        extentReportSpark();
//        StartBrowser()
        autoOpenBrowser();
    }

    @Before
    public void before(){

    }
    @After
    public void after(Scenario scenario){
        System.out.println("After suite..."+scenario.getName());
    }
    @AfterAll
    public static void afterAll(){

    }


}
