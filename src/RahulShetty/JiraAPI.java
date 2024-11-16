package RahulShetty;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class JiraAPI {

	public String ID;
	
	@Test
	public void createBug() {
		
		// Bug Creation
		RestAssured.baseURI = "https://chandrahas-g.atlassian.net/";
		String response = given().header("Content-Type", "application/json")
		.header("Authorization", "Basic Y2hhbmRyYWhhc2FnYWp1bGExOTk5QGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBGaGJvS3k1Q3ZSU1ZNSnNxT2c5aEFoRkZTSTFJdkN2R2g5X0QzdVA1SzRUTGlVMXRRUng0aG44VDdQc1dZbTBVZExpNGJPVk5Tb09SelRPSlRPWVVteWNsRXVTUm15R3NaaDBqcmlvWVZ4YzI2dGxQMVl3bXRncnYxMzV4T295SF9sdm5WcWt3Y1hISkc5aVVsT280cUZVcGpkSDg5N2cxQ0pwMDRMMGFsZHM9OEREQTA0MjM=")
		.body("{\r\n"
				+ "    \"fields\": {\r\n"
				+ "       \"project\":\r\n"
				+ "       {\r\n"
				+ "          \"key\": \"SCRUM\"\r\n"
				+ "       },\r\n"
				+ "       \"summary\": \"Button is not working.\",\r\n"
				+ "       \"issuetype\": {\r\n"
				+ "          \"name\": \"Bug\"\r\n"
				+ "       }\r\n"
				+ "   }\r\n"
				+ "}\r\n"
				+ "")
		.post("rest/api/3/issue")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath jp = new JsonPath(response);
		System.out.println(jp.getString("id"));
		ID = jp.getString("id");
	}
	
	@Test(dependsOnMethods = "createBug")
	public void attachScreenshot() {
				
		RestAssured.baseURI = "https://chandrahas-g.atlassian.net/";
		given().pathParam("key", ID)
		.header("Content-Type", "multipart/form-data; boundary=<calculated when request is sent>")
		.header("X-Atlassian-Token", "no-check")
		.header("Authorization", "Basic Y2hhbmRyYWhhc2FnYWp1bGExOTk5QGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBGaGJvS3k1Q3ZSU1ZNSnNxT2c5aEFoRkZTSTFJdkN2R2g5X0QzdVA1SzRUTGlVMXRRUng0aG44VDdQc1dZbTBVZExpNGJPVk5Tb09SelRPSlRPWVVteWNsRXVTUm15R3NaaDBqcmlvWVZ4YzI2dGxQMVl3bXRncnYxMzV4T295SF9sdm5WcWt3Y1hISkc5aVVsT280cUZVcGpkSDg5N2cxQ0pwMDRMMGFsZHM9OEREQTA0MjM=")
		.multiPart("file", new File("C:\\Users\\chand\\Pictures\\pexels-matthew-montrone-230847-1324803.jpg"))
		.post("rest/api/3/issue/{key}/attachments")
		.then().log().all().assertThat().statusCode(200);
	}
}
