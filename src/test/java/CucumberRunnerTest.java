import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
        plugin = {"junit:target/cucumber.xml", "pretty"},
        features = "src/test/java/features",
        glue = {"steps"}
)
public class CucumberRunnerTest extends AbstractTestNGCucumberTests {

}