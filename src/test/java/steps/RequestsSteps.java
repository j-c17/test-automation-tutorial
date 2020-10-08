package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.TestConfiguration;
import utils.TestInitialization;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertEquals;

public class RequestsSteps {


    private static Response response;

    @Given("API Client is initialized")
    public void initAPIClient() {
        TestInitialization.init();
        RestAssured.baseURI = TestConfiguration.getHost();
    }

    @Given("User is initialized")
    public void userIsInitialized() {
        System.out.println("Access granted");
    }

    @Given("Service is available")
    public void checkService() {
        assertEquals(RestAssured.get().getStatusCode(), 200);
    }

    @When("I execute GET {word} request")
    public void executeRequest(String endpoint) {
        response = RestAssured.get(endpoint);
    }

    @Then("I should receive {int} response code")
    public void shouldReceiveCode(int code) {
        response.then().assertThat().statusCode(code);
    }

    @And("Response body is matched to {word}")
    public void responseBodyIsMatchedToSchemaJson(String schemaName) {
        response.then().body(matchesJsonSchemaInClasspath(schemaName));
    }
}
