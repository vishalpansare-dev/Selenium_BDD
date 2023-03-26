package Helpers;

import TestBase.BaseClass;
import io.cucumber.java.Scenario;
import org.openqa.selenium.By;

import java.util.Collection;

public class CommonUtils extends BaseClass {
    private CommonUtils(){}

    public static Ipage getPageByName(String pageName){
        try {
            return  (Ipage) Class.forName("PageObjects." + pageName).newInstance();
        } catch (ClassNotFoundException e) {
            test.fail(e);
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            test.fail(e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            test.fail(e);
            throw new RuntimeException(e);
        }
    }
    public static By getLocatorByName(Class pageRepoClass,String locator){
        try {
            return  (By) pageRepoClass.getDeclaredField(locator).get(null);

        } catch (NoSuchFieldException e) {
            test.fail(e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            test.fail(e);
            throw new RuntimeException(e);
        }
    }

    public static void createTest(Scenario scenario) {
        Collection<String> Tags = scenario.getSourceTagNames();
        test = extent.createTest(scenario.getName());
        for (String tag: Tags) {
            if(tag.startsWith("@Category"))
                test.assignCategory(tag);
            if(tag.startsWith("@Author"))
                test.assignAuthor(tag);
        }

    }
}
