package StepDef;

import Helpers.CommonUtils;
import TestBase.BaseClass;
import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
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
    public void before(Scenario scenario){
        CommonUtils.createTest(scenario);
    }
    @After
    public void after(Scenario scenario){
        if(scenario.isFailed()){
            test.fail(scenario.getName() + " : failed due to above error. Please refer to the screenshot at the Top.");
            String screenshotBase64 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
            test.addScreenCaptureFromBase64String(screenshotBase64);
        }else {
            test.pass(scenario.getName()+" : Passed");
        }

    }
    @AfterAll
    public static void afterAll(){

            driver.close();

        if(extent!=null) {
            extent.flush();
        }

    }


}
