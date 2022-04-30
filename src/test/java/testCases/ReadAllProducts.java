package testCases;

import static io.restassured.RestAssured.given;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadAllProducts {

	SoftAssert softAssert;
	
	@Test
	public void readAllProducts() {
		
		/*
		   given: all input details(base URI,Headers,Payload/Body,QueryParameters,Authorization)
		   when: submit api requests(Http method,Endpoint/Resource)
		   then: validate response(status code, Headers, responseTime, Payload/Body)
		   
		   Endpoint_url:https://techfios.com/api-prod/api/product/read.php 
		   http method: GET
		   QueryParameters: No
		   Authorization: No Headers: No Content-Type: application/json; charset=UTF-8
		   Body:No Status Code: 200 OK
		 */
		
		softAssert = new SoftAssert();
		
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type", "application/json; charset=UTF-8")
								.auth().preemptive().basic("demo@techfios.com", "abc123").
							when()
								.get("/read.php").
							then()
								.extract().response();
		
		int actualResponseStatus = response.getStatusCode();
		System.out.println("Actual Response Status: " + actualResponseStatus);
		softAssert.assertEquals(actualResponseStatus, 200);
		
		String actualResponseContentType = response.getHeader("Content-Type");
		System.out.println("Actual Response Content Type: " + actualResponseContentType);
		softAssert.assertEquals(actualResponseContentType, "application/json; charset=UTF-8");
		
		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		String firstProductId = jp.get("records[0].id");
		if(firstProductId != null) {
			System.out.println("Product exist");
		}else {
			System.out.println("Product doesn't exist!");
		}
		
	}

}
