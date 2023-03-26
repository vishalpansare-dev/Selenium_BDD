package TestRunner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/Features",
        glue={"StepDef"},
        plugin = { "pretty", "html:target/reports/HTML/cucumber-reports.html","json:target/reports/JSON/cucumber.json"  },
//        dryRun = true,
        tags = "@test"
//        monochrome = true,


)
public class TestRunner {
}
