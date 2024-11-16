package RahulShetty;

import org.testng.Assert;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJson {

	public static void main(String[] args) {

		JsonPath jp = new JsonPath(Payload.mockJsonResponse());

		// No. of Courses
		System.out.println("No. of Courses: " + jp.getInt("courses.size()"));

		// Purchase Amount
		System.out.println("Total Amount: " + jp.getInt("dashboard.purchaseAmount"));

		// Title of First Course
		System.out.println(jp.get("courses[0].title").toString());

		// All courses & their respective prices
		System.out.println("\nCourses & Prices:");
		for (int i = 0; i < jp.getInt("courses.size()"); i++) {
			System.out.println(jp.getString("courses[" + i + "].title") + " " + jp.getInt("courses[" + i + "].price"));
		}

		// No. of Copies sold
		System.out.println("\nNo. of Copies sold:");
		for (int i = 0; i < jp.getInt("courses.size()"); i++) {
			System.out.println(jp.getString("courses[" + i + "].title") + " " + jp.getInt("courses[" + i + "].copies"));
		}

		// Copies of RPA Course
		for (int i = 0; i < jp.getInt("courses.size()"); i++) {
			if (jp.getString("courses[" + i + "].title").equalsIgnoreCase("RPA")) {
				System.out.println("\nCopies of RPA Course"+jp.getInt("courses[" + i + "].copies"));
				break;
			}
		}

		// Total No. of Copies sold
		int totalCopies = 0;
		for (int i = 0; i < jp.getInt("courses.size()"); i++)
			totalCopies += jp.getInt("courses[" + i + "].copies");
		System.out.println("\nTotal No. of Copies sold are " + totalCopies);

		// Sum of all Course prices matches with Purchase Amount
		int totalAmount = 0;
		for (int i = 0; i < jp.getInt("courses.size()"); i++) {
			totalAmount += jp.getInt("courses[" + i + "].price") * jp.getInt("courses[" + i + "].copies");
		}
		System.out.println("\nTotal Amount is " + totalAmount);

		Assert.assertEquals(totalAmount, jp.getInt("dashboard.purchaseAmount"));
	}
}
