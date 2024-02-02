package ramafinal;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class Testing {
	 @Test
	    public void testLogin() {
		 	String input = "rama\nAsdfgh9899!\n"; // Set your desired input here
	        InputStream targetStream = new ByteArrayInputStream(input.getBytes());

	        Scanner scanner = new Scanner(targetStream);
	        String enteredUsername = "";
	        String enteredPassword = "";
	        String[] loginResult = rama.login(scanner, enteredUsername, enteredPassword);
	        String actual = loginResult[0];
	        String expected = "Login successful";
	        assertEquals(expected.trim(), actual.trim());

	        scanner.close();
	    }
	 
	 @Test
	    public void testLogin1() {
		 	String input = "lina\n123\n"; // Set your desired input here
	        InputStream targetStream = new ByteArrayInputStream(input.getBytes());

	        Scanner scanner = new Scanner(targetStream);
	        String enteredUsername = "";
	        String enteredPassword = "";
	        String[] loginResult = rama.login(scanner, enteredUsername, enteredPassword);
	        String actual = loginResult[0];
	        String expected = "Login successful";
	        assertEquals(expected.trim(), actual.trim());

	        scanner.close();
	    }
	 
	 @Test
	 public void testLogin2() {
	     String input = "Manal\n123\nManal\n123\nManal\n123\nManal\n123\nManal\n123\n"; // Repeated invalid login attempts
	     InputStream targetStream = new ByteArrayInputStream(input.getBytes());
	     Scanner scanner = new Scanner(targetStream);
	     String enteredUsername = "";
	     String enteredPassword = "";
	     String[] loginResult = rama.login(scanner, enteredUsername, enteredPassword);
	     
	     // Ensure that the message is set only after the maximum number of attempts
	     assertEquals("Invalid username or password. Please try again.", loginResult[0].trim());
	     
	     scanner.close();
	 }




	// -----------------------------------------------------------------
	@Test
	public void testViewMedicalRecord() {
		String enteredUsername = "dima";
		String expected = "There is no medical information for you";
		String actual = rama.viewMedicalRecord(enteredUsername);
		assertEquals(expected.trim(), actual.trim());
	}
	
	@Test
	public void testViewMedicalRecord2() {
		String enteredUsername = "lina";
		String expected = "Username: lina, Date of next visit: 2-3-2024, Patient diagnosis: CKD";
		String actual = rama.viewMedicalRecord(enteredUsername);
		assertEquals(expected.trim(), actual.trim());
	}

	/*// -----------------------------------------------------------------
	@Test
	public void testviewpatPersonalInformation() {
		String enteredUsername = "lina";
		String expected = "Username: lina, "
				+ "Password: a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3, "
				+ "Phone Number: 079, Gender: f, Age: 20";
		String actual = rama.viewpatPersonalInformation(enteredUsername);
		assertEquals(expected.trim(), actual.trim());
	}

	// -----------------------------------------------------------------

	@Test
	public void testviewdocPersonalInformation() {
		String enteredUsername = "sara";
		String expected = "Username: sara, "
				+ "Password: a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3, "
				+ "Phone Number: 079, Gender: f, Age: 40";
		String actual = rama.viewdocPersonalInformation(enteredUsername);
		assertEquals(expected.trim(), actual.trim());
	}*/
}
