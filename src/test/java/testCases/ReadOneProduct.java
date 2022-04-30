package testCases;

import static io.restassured.RestAssured.given;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneProduct {

	SoftAssert softAssert;
	
	public ReadOneProduct() {
		softAssert = new SoftAssert();
	}
	
	@Test
	public void readOneProduct() {
			
		/*
		    given: all input details(base URI,Headers,Payload/Body,QueryParameters,Authorization)
		    when: submit api requests(Http method,Endpoint/Resource)
		    then: validate response(status code, Headers, responseTime, Payload/Body)
		    
			EndPoint_Url: https://techfios.com/api-prod/api/product/read_one.php?id=65
			HTTP method : GET
			Authorization: basic auth
			Query Parameters: id=4097
			Header/headers:
			Content_Type: application/json
			status code: 200
		 */
		
		Response response = given()
								.baseUri("https://techfios.com/api-prod/api/product")
								.header("Content-Type", "application/json")
								.auth().preemptive().basic("demo@techfios.com", "abc123")
								.queryParam("id", "4124").
							when()
								.get("/read_one.php").
							then()
								.extract().response();
		
		int actualResponseStatus = response.getStatusCode();
		System.out.println("Actual Response Status: " + actualResponseStatus);
		softAssert.assertEquals(actualResponseStatus, 200, "Status codes are not matching!");
		
		String actualResponseContentType = response.getHeader("Content-Type");
		System.out.println("Actual Response Content Type: " + actualResponseContentType);
		softAssert.assertEquals(actualResponseContentType, "application/json", "Response Content-Type are not matching!");
		
		String actualResponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + actualResponseBody);
		
		JsonPath jp = new JsonPath(actualResponseBody);
		String productID = jp.get("id");
		softAssert.assertEquals(productID, "4124", "Product ID not matching!");
		
		String productName = jp.get("name");
		softAssert.assertEquals(productName, "Prabin's Amazing Pillow 2.0", "Product names are not matching!");
		
		String productPrice = jp.get("price");
		System.out.println("Product Price : " + productPrice);
		softAssert.assertEquals(productPrice, "299", "Product prices are not matching!");
				
		softAssert.assertAll();
	
	}

}
