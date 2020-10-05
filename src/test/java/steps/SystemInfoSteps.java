package steps;

import io.cucumber.java.en.Then;
import utils.TestConfiguration;

import java.time.LocalDate;
import java.time.LocalTime;

public class SystemInfoSteps {

    @Then("System date is printed")
    public void systemDateIsPrinted() {
        System.out.println(LocalDate.now().toString());
    }

    @Then("System time is printed")
    public void systemTimeIsPrinted() {
        System.out.println(LocalTime.now().toString());
    }

    @Then("OS is printed")
    public void osIsPrinted() {
        System.out.println(System.getProperty("os.name"));
    }

    @Then("Project localization is printed")
    public void projectLocalizationIsPrinted() {
        System.out.println(System.getProperty("user.dir"));
    }

    @Then("Host is printed")
    public void hostIsPrinted() {
        System.out.println(TestConfiguration.getHost());
    }
}
