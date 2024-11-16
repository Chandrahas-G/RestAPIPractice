package RahulShetty;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import POJO.GetCourseDetails;
import io.restassured.path.json.JsonPath;

public class OAuth {

	static String[] courses = {"Selenium Webdriver Java","Cypress","Protractor"};
	public static void main(String[] args) throws InterruptedException {

		String response = given()
				.formParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.formParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W").formParams("grant_type", "client_credentials")
				.formParams("scope", "trust").when()
				.post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token").asString();

		// System.out.println(response);

		// JsonPath jsonPath = new JsonPath(response);
		String accessToken = new JsonPath(response).getString("access_token");
		System.out.println(accessToken);

		// Deserialization using POJO Class
		GetCourseDetails gcd = given().queryParams("access_token", accessToken).when()
				.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(GetCourseDetails.class);

		System.out.println(gcd.getLinkedIn());
		System.out.println(gcd.getCourses().getApi().get(0).getCourseTitle());

		// get price details
		for (int i = 0; i < gcd.getCourses().getApi().size(); i++) {
			if (gcd.getCourses().getApi().get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				System.out.println(gcd.getCourses().getApi().get(i).getPrice());
				break;
			}
		}

		// get course details present in webAutomation & verify with expected
		ArrayList<String> Actual = new ArrayList<String>();
		List<String> Expected = Arrays.asList(courses);
		
		for (int i = 0; i <= gcd.getCourses().getApi().size(); i++) {
			Actual.add(gcd.getCourses().getWebAutomation().get(i).getCourseTitle());
		}
		
		Assert.assertEquals(Actual, Expected);
	}
}
