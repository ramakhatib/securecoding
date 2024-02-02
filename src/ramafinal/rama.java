package ramafinal;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class rama {

	private static final int MAX_PASSWORD_LENGTH = 20;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int choice;

		do {
			System.out.println("Choose an option:");
			System.out.println("1. Login");
			System.out.println("2. Sign Up");
			System.out.println("3. Exit");
			System.out.print("Enter your choice: ");

			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				String enteredUsername = "";
				String enteredPassword = "";
				String result = "";
				String[] loginResult = login(scanner, enteredUsername, enteredPassword);
				result = loginResult[0];
				enteredUsername = loginResult[1];

				if (result.equals("Login successful")) {
					boolean foundInDoctors = searchUsernameInFile(enteredUsername,
							"C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\doctors.txt");
					boolean foundInPatients = searchUsernameInFile(enteredUsername,
							"C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\patients.txt");

					if (foundInDoctors) {
						DoctorOptions(scanner, enteredUsername);
					} else if (foundInPatients) {
						PatientOptions(scanner, enteredUsername);
					} else {
						RegistrarOptions(scanner);
					}
				}
				break;

			case 2:
			signUp(scanner);
				break;
			case 3:
				System.exit(0);
			default:
				System.out.println("Invalid choice. Please choose either 1 or 2.");
				break;
			}
		} while (choice != 3);

		scanner.close();
	}

	public static String[] login(Scanner scanner, String enteredUsername, String enteredPassword) {
		
		boolean loginSuccess = false;
		int attempts = 0;
		int MAX_ATTEMPTS = 5;
		String[] loginResult = new String[2];
		loginResult[0] = "";
		loginResult[1] = "";
		while (!loginSuccess && attempts < MAX_ATTEMPTS) {
			System.out.print("Enter username: ");
			enteredUsername = scanner.nextLine();

			System.out.print("Enter password: ");
			enteredPassword = scanner.nextLine();

			String hashedEnteredPassword = getHash(enteredPassword);

			try (FileReader fileReader = new FileReader(
					"C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\users.txt");
					BufferedReader bufferedReader = new BufferedReader(fileReader)) {

				String line;

				while ((line = bufferedReader.readLine()) != null) {
					String[] user = line.split(",");
					String storedUsername = user[0];
					String storedPassword = user[1];

					if (enteredUsername.equals(storedUsername) && hashedEnteredPassword.equals(storedPassword)) {
						loginSuccess = true;
						loginResult[0] = "Login successful";
						loginResult[1] = enteredUsername;
						break;
					}
					MyLogger.writeToLog("A user logged in");
				}
			} catch (IOException ex) {
				System.out.println("Error reading file: " + ex.getMessage());
			}
			

			if (!loginSuccess) {
				attempts++;
				System.out.println("Invalid username or password. Please try again." + (MAX_ATTEMPTS - attempts)
						+ " attempts Remaining");
				loginResult[0] ="Invalid username or password. Please try again.";
				loginResult[1] = enteredUsername;
			}
			
		}

		if (!loginSuccess) {
			System.out.println("Exceeded maximum login attempts. Please contact support.");
		}
		return loginResult;

	}

	public static void signUp(Scanner scanner) {
		System.out.print("Enter the Admin code to sign up as a resgisteror: ");
		String admincode = "";
		admincode = scanner.nextLine();
		if (admincode.equals("Asdfgh9899!")) {
			System.out.print("Enter new username: ");
			String newUsername = scanner.nextLine();
			if (newUsername.length() > MAX_USERNAME_LENGTH) {
				System.out
						.println("Invalid username length. Maximum length is " + MAX_USERNAME_LENGTH + " characters.");
				return;
			}

			boolean findingusername = checkUsername(newUsername,
					"C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\users.txt");
			String newPassword;
			if (findingusername) {
				do {
					System.out.print(
							"Enter new password (at least 8 characters, containing at least one digit and one special character): ");
					newPassword = scanner.nextLine();
				} while (!checkPassword(newPassword));
				// Sanitization
				if (newPassword.length() > MAX_PASSWORD_LENGTH) {
					System.out.println(
							"Invalid password length. Maximum length is " + MAX_PASSWORD_LENGTH + " characters.");
					return;
				}

				String hashedPassword = getHash(newPassword);

				try (FileWriter fileWriter = new FileWriter(
						"C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\users.txt",
						true);
						BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
						PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

					printWriter.println(newUsername + "," + hashedPassword);
				} catch (IOException e) {
					System.out.println("Error writing to file: " + e.getMessage());
				}
			} else {
				System.out.println("Username already exists. Try another one.");
			}
		} else {
			System.out.println("Admin code not correct, try again");

		}

	}

	private static void RegistrarOptions(Scanner scanner) {
		int choice;
		do {
			System.out.println("Registrar Menu:");
			System.out.println("1. Register new patient");
			System.out.println("2. Register new doctor");
			System.out.println("3. Exit");
			choice = scanner.nextInt();
			switch (choice) {
			case 1:
				// Perform registrar-specific operations
				registerNewPatient(scanner);
				break;
			case 2:
				registerNewDoctor(scanner);
				break;
			case 3:
				break;
			default:
				System.out.println("Invalid choice. Please choose either 1 or 2.");
				break;
			}
		} while (choice != 3);
	}

	private static void DoctorOptions(Scanner scanner, String enteredUsername) {
		int choice;
		do {
			System.out.println("Doctor Menu:");
			System.out.println("1. View personal information");
			System.out.println("2. Enter medical inf1ormation of a patient");
			System.out.println("3. Exit");
			choice = scanner.nextInt();

			switch (choice) {
			case 1:
				String result = viewdocPersonalInformation(enteredUsername);
				System.out.println("Your Personal information record: " + result);
				break;
			case 2:
				// Perform doctor-specific operations
				enterMedicalInformation(scanner);
				break;
			default:
				System.out.println("Invalid choice. Please choose either 1 or 2.");
				break;
			}

		} while (choice != 3);
	}

	private static void PatientOptions(Scanner scanner, String enteredUsername) {

		int choice;
		do {
			System.out.println("Patient Menu:");
			System.out.println("1. View personal information");
			System.out.println("2. View medical record");
			System.out.println("3. Exit");

			choice = scanner.nextInt();

			switch (choice) {
			case 1:
				String result1 = viewpatPersonalInformation(enteredUsername);
				System.out.println("Your Personal information record: " + result1);
				break;
			case 2:
				String result = viewMedicalRecord(enteredUsername);
				System.out.println(result);
				break;
			default:
				System.out.println("Invalid choice. Please select a valid option.");
				break;
			}
		} while (choice != 3);
	}

	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	public static void registerNewPatient(Scanner scanner) {
		String patUsername;
		String patPass;
		String patPhone;
		int patAge;
		String patGender;

		System.out.println("Enter Patient Information:");

		scanner.nextLine();
		System.out.println("Username: ");
		patUsername = scanner.nextLine();

		boolean findingusername = checkUsername(patUsername,
				"C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\users.txt");
		if (findingusername) {

			   do {
	                System.out.println("Enter new password (at least 8 characters, containing at least one digit and one special character): ");
	                patPass = scanner.nextLine();
	            } while (!isStrongPassword(patPass));
			
			String hashedPassword = getHash(patPass);

			System.out.println("Phone Number: ");
			patPhone = scanner.nextLine();

			System.out.println("Gender: ");
			patGender = scanner.nextLine();

			System.out.println("Age: ");
			patAge = scanner.nextInt();
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						"C:\\\\Users\\\\User\\\\OneDrive\\\\Desktop\\\\Secure coding\\\\Final\\\\Finalcode\\\\src\\\\ramafinal\\\\patients.txt",
						true));
				// Writing the doctor's information to the file
				writer.write("Username: " + patUsername + ", Password: " + hashedPassword + ", Phone Number: "
						+ patPhone + ", Gender: " + patGender + ", Age: " + patAge + "\n");

				writer.close(); // Close the writer to release resources
			} catch (IOException e) {
				System.err.println("Error: " + e.getMessage());
			}

			try (

					FileWriter fileWriter = new FileWriter(
							"C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\users.txt",
							true);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

				printWriter.println(patUsername + "," + hashedPassword);
				System.out.println("Patient information added successfully.");
			} catch (IOException e) {
				System.out.println("Error writing to file: " + e.getMessage());
			}
			
			MyLogger.writeToLog("A new patient was added" + patUsername);
		} else {
			System.out.println("Username alredy exist try another one");
		}

	}

	  public static void registerNewDoctor(Scanner scanner) {
	        System.out.println("Enter Doctor Information:");
	        String docUsername;
	        String docPass;
	        String docPhone;
	        int docAge;
	        String docGender;
	        scanner.nextLine(); // Consume the newline character

	        System.out.println("Username: ");
	        docUsername = scanner.nextLine();
	        boolean findingUsername = checkUsername(docUsername,
	                "C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\users.txt");
	        if (findingUsername) {

	            do {
	                System.out.println("Enter new password (at least 8 characters, containing at least one digit and one special character): ");
	                docPass = scanner.nextLine();
	            } while (!isStrongPassword(docPass));

	            String hashedPassword = getHash(docPass);

	            System.out.println("Phone Number: ");
	            docPhone = scanner.nextLine();

	            System.out.println("Gender: ");
	            docGender = scanner.nextLine();

	            System.out.println("Age: ");
	            docAge = scanner.nextInt();
	            try {
	                // Writing the doctor's information to the file
	                BufferedWriter writer = new BufferedWriter(new FileWriter(
	                        "C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\doctors.txt",
	                        true));
	                writer.write("Username: " + docUsername + ", Password: " + hashedPassword + ", Phone Number: "
	                        + docPhone + ", Gender: " + docGender + ", Age: " + docAge + "\n");

	                writer.close(); 
	                MyLogger.writeToLog("A new doctor was added" +docUsername);

	                try (
	                        FileWriter fileWriter = new FileWriter(
	                                "C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\users.txt",
	                                true);
	                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	                        PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

	                    printWriter.println(docUsername + "," + hashedPassword);
	                    System.out.println("Doctor information added successfully.");
	                } catch (IOException e) {
	                    System.out.println("Error writing to file: " + e.getMessage());
	                }
	            } catch (IOException e) {
	                System.err.println("Error: " + e.getMessage());
	            }
	        } else {
	            System.out.println("Username already exists. Try another one.");
	        }
	    }

	    private static boolean isStrongPassword(String password) {
	        return password.length() >= 8 && password.matches(".*[a-zA-Z]+.*") && password.matches(".*\\d+.*") && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]+.*");
	    }

	    // Implement your checkUsername and getHash methods as in your original code
	

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*/

	public static String viewdocPersonalInformation(String username) {
		String result = null;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					"C:\\\\Users\\\\User\\\\OneDrive\\\\Desktop\\\\Secure coding\\\\Final\\\\Finalcode\\\\src\\\\ramafinal\\\\doctors.txt"));
			String line;
			StringBuilder test = new StringBuilder(); // StringBuilder to store the lines
			boolean found = false;

			while ((line = reader.readLine()) != null) {
				if (line.contains("Username: ") && line.contains(username)) {
					test.append(line).append("\n"); // Append the line to the StringBuilder
					result = test.toString();
					found = true;
					break;
				}
			}

			reader.close();

			if (!found) {
				System.out.println("Doctor with username '" + username + "' not found.");
			}
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return result;
	}
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	private static void enterMedicalInformation(Scanner scanner) {
		String patUsername;
		String nextvist;
		String Patientdiagnosis;
		scanner.nextLine(); // Consume the newline character

		System.out.println("Enter the patient Username:");
		patUsername = scanner.nextLine();
		boolean findingusername = searchUsernameInFile(patUsername,
				"C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\patients.txt");

		if (findingusername) {

			System.out.println("Date of next visit: ");
			nextvist = scanner.nextLine();

			System.out.println("Patient diagnosis: ");
			Patientdiagnosis = scanner.nextLine();

			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						"C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\medrecords.txt",
						true));
				// Writing the doctor's information to the file
				writer.write("Username: " + patUsername + ", Date of next visit: " + nextvist + ", Patient diagnosis: "
						+ Patientdiagnosis + "\n");

				writer.close(); // Close the writer to release resources
			} catch (IOException e) {
				System.err.println("Error: " + e.getMessage());
			}
		} else {
			System.out.println("Username does not exist try another one");
		}
	}

	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*/

	public static String viewpatPersonalInformation(String username) {
		String result = null;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					"C:\\\\Users\\\\User\\\\OneDrive\\\\Desktop\\\\Secure coding\\\\Final\\\\Finalcode\\\\src\\\\ramafinal\\\\patients.txt"));
			String line;
			StringBuilder test = new StringBuilder(); // StringBuilder to store the lines

			boolean found = false;

			while ((line = reader.readLine()) != null) {
				if (line.contains("Username: ") && line.contains(username)) {
					test.append(line).append("\n"); // Append the line to the StringBuilder
					result = test.toString();
					found = true;
					break;
				}
			}

			reader.close();

			if (!found) {
				System.out.println("Doctor with username '" + username + "' not found.");
			}
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return result;

	}

	public static String viewMedicalRecord(String username) {
		String result = null;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					"C:\\Users\\User\\OneDrive\\Desktop\\Secure coding\\Final\\Finalcode\\src\\ramafinal\\medrecords.txt"));

			StringBuilder test = new StringBuilder(); // StringBuilder to store the lines

			String line;
			boolean found = false;

			while ((line = reader.readLine()) != null) {
				if (line.contains("Username: ") && line.contains(username)) {
					test.append(line).append("\n"); // Append the line to the StringBuilder
					result = test.toString();
					found = true;
					break;
				}
			}

			reader.close();

			if (!found) {
				result = "There is no medical information for you";
			}

		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return result;
	}

	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	private static boolean checkPassword(String password) {
		if (password.length() < 8) {
			System.out.println("Password must be at least 8 characters long.");
			return false;
		}
		if (!password.matches(".*\\d.*")) {
			System.out.println("Password must contain at least one digit.");
			return false;
		}
		if (!password.matches(".*[!@#$%^&*()\\-+_=\\[\\]{};':\",./<>?].*")) {
			System.out.println("Password must contain at least one special character.");
			return false;
		}
		return true;
	}

	private static boolean searchUsernameInFile(String username, String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("Username: " + username + ", ")) {
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean checkUsername(String username, String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");

				if (parts.length == 2 && parts[0].trim().equals(username)) {
					return false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private static String getHash(String value) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			result = encode(md.digest(value.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("The algorithm does not exist");
		}
		return result;
	}

	private static String encode(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
	
}
