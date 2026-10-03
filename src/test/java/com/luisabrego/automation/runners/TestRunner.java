package com.luisabrego.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Runs every feature through TestNG. Scenarios run in parallel; the number of threads
 * comes from the "threads" property in pom.xml (mvn test -Dthreads=4).
 * Filter scenarios with tags: mvn test -Dcucumber.filter.tags="@api and @smoke"
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.luisabrego.automation.steps",
                "com.luisabrego.automation.hooks"
        },
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "html:target/cucumber-report.html",
                "rerun:target/failed-scenarios.txt"
        },
        monochrome = true)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
