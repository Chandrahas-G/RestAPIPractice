package RahulShetty;

import static io.restassured.RestAssured.given;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Library {

	// Add Books
	@Test(dataProvider = "BooksData")
	public void AddBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		Response response = given().header("Content-Type", "application/json").body(Payload.addBook(isbn, aisle)).when()
				.post("/Library/Addbook.php").then().assertThat().statusCode(200).extract().response();
		String responseString = response.asString();
		JsonPath jp = new JsonPath(responseString);
		String id = jp.get("ID");
		System.out.println(id);

		// Delete Books
		Response delResponse = given().header("Content-Type", "application/json")
				.body("{\r\n" + " \r\n" + "\"ID\" : \"" + id + "\"\r\n" + " \r\n" + "} ").when()
				.post("/Library/DeleteBook.php").then().assertThat().statusCode(200).extract().response();
		String delResponseString = delResponse.asString();
		JsonPath jp1 = new JsonPath(delResponseString);
		System.out.println(jp1.getString("msg"));
	}

	@DataProvider(name = "BooksData")
	public Object[][] getData() {
		// array=collection of elements
		// multidimensional array= collection of arrays
		Object[][] obj = new Object[][] { { "ojsfwty", "9163" }, { "cswetee", "4153" }, { "okmsfet", "533" } };
		return obj;
	}
}
