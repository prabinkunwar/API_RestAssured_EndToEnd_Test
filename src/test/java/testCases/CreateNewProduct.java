package testCases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateNewProduct {

	SoftAssert softAssert = new SoftAssert();
	
	public CreateNewProduct() {
		softAssert = new SoftAssert();
	}
	
	@Test
	public void createNewProduct() {
			
		/*
		    given: all input details(base URI,Headers,Payload/Body,QueryParameters,Authorization)
		    when: submit api requests(Http method,Endpoint/Resource)
		    then: validate response(status code, Headers, responseTime, Payload/Body)
		    
			EndPoint_Url: https://techfios.com/api-prod/api/product/create.php
			HTTP method : POST
			Authorization: basic auth
			Header/headers:
			Content_Type: application/json; charset=UTF-8
			status code: 201
			body/payload:
			{
    			"name" : "Prabin's Amazing Pillow 2.0",
    			"price" : "299",
    			"description" : "The best pillow for amazing automation engineers.",
    			"category_id" : "2"
			}
		 */
		
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("name", "Prabin's Amazing Pillow 2.0");
		payload.put("price", "299");
		payload.put("description", "The best pillow for amazing automation engineers.");
		payload.put("category_id", "2");
		
		String productName = payload.get("name");
		System.out.println("Product Name: " + productName);
		
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type", "application/json; charset=UTF-8")
								.auth().preemptive().basic("demo@techfios.com", "abc123")
								.body(payload).
							when()
								.post("/create.php").
							then()
								.extract().response();
		
		int actualResponseStatus = response.getStatusCode();
		System.out.println("Actual Response Status: " + actualResponseStatus);
		softAssert.assertEquals(actualResponseStatus, 201, "Status codes are not matching!");
		
		String actualResponseContentType = response.getHeader("Content-Type");
		System.out.println("Actual Response Content Type: " + actualResponseContentType);
		softAssert.assertEquals(actualResponseContentType, "application/json; charset=UTF-8", "Response Content-Type are not matching!");
		
		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		String productMessage = jp.get("message");
		softAssert.assertEquals(productMessage, "Product was created.", "Product message's are not matching!");

				
		softAssert.assertAll();
	
	}

}
