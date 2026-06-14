// BankingClient.java - No external libraries required
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class BankingClient {
    private static final String BASE_URL = "http://localhost:8000/api";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static String currentUserJson = null;
    private static int currentUserId = 0;
    private static String currentUsername = "";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            while (true) {
                clearScreen();
                printHeader();
                
                if (currentUserId == 0) {
                    showMainMenu();
                } else {
                    showUserDashboard();
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showMainMenu() throws Exception {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│              MAIN MENU                  │");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. Login                                │");
        System.out.println("│ 2. Register New Account                 │");
        System.out.println("│ 3. Exit                                 │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Choose an option: ");
        
        int choice = Integer.parseInt(scanner.nextLine());
        
        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleRegistration();
                break;
            case 3:
                System.out.println("\nThank you for using Banking System. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("\nInvalid option!");
                pressEnter();
        }
    }

    private static void showUserDashboard() throws Exception {
        System.out.println("\nWelcome back, " + currentUsername + "!");
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│           USER DASHBOARD                │");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. View Profile                         │");
        System.out.println("│ 2. Check Balance                        │");
        System.out.println("│ 3. Logout                               │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Choose an option: ");
        
        int choice = Integer.parseInt(scanner.nextLine());
        
        switch (choice) {
            case 1:
                viewProfile();
                break;
            case 2:
                checkBalance();
                break;
            case 3:
                logout();
                break;
            default:
                System.out.println("\nInvalid option!");
        }
        pressEnter();
    }

    private static void handleLogin() throws Exception {
        clearScreen();
        System.out.println("\n=== LOGIN ===\n");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        String jsonBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/login/"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            // Parse JSON manually to avoid external libraries
            String responseBody = response.body();
            currentUserId = extractIntValue(responseBody, "\"id\":");
            currentUsername = extractStringValue(responseBody, "\"username\":");
            currentUserJson = responseBody;
            System.out.println("\n✓ Login successful!");
        } else {
            System.out.println("\n✗ Login failed! Invalid credentials.");
        }
    }

    private static void handleRegistration() throws Exception {
        clearScreen();
        System.out.println("\n=== REGISTRATION ===\n");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password (min 6 chars): ");
        String password = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        
        String jsonBody = String.format("{\"username\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"phone\":\"%s\"}", 
                                       username, email, password, phone);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/register/"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            String responseBody = response.body();
            currentUserId = extractIntValue(responseBody, "\"id\":");
            currentUsername = extractStringValue(responseBody, "\"username\":");
            currentUserJson = responseBody;
            System.out.println("\n✓ Registration successful!");
        } else {
            System.out.println("\n✗ Registration failed!");
        }
    }

    private static void checkBalance() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/user/" + currentUserId + "/balance/"))
            .GET()
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            String responseBody = response.body();
            double balance = extractDoubleValue(responseBody, "\"balance\":");
            System.out.printf("\n✓ Your current balance: $%.2f%n", balance);
        } else {
            System.out.println("\n✗ Failed to fetch balance!");
        }
    }

    private static void viewProfile() {
        System.out.println("\n=== USER PROFILE ===\n");
        System.out.println("User ID: " + currentUserId);
        System.out.println("Username: " + currentUsername);
        
        // Extract more details from stored JSON
        if (currentUserJson != null) {
            String email = extractStringValue(currentUserJson, "\"email\":");
            String accountNumber = extractStringValue(currentUserJson, "\"account_number\":");
            double balance = extractDoubleValue(currentUserJson, "\"balance\":");
            String phone = extractStringValue(currentUserJson, "\"phone\":");
            
            System.out.println("Email: " + email);
            System.out.println("Account Number: " + accountNumber);
            System.out.printf("Balance: $%.2f%n", balance);
            System.out.println("Phone: " + phone);
        }
    }

    private static void logout() {
        currentUserId = 0;
        currentUsername = "";
        currentUserJson = null;
        System.out.println("\n✓ Logged out successfully!");
    }

    // Helper methods to parse JSON without external libraries
    private static String extractStringValue(String json, String key) {
        int startIndex = json.indexOf(key);
        if (startIndex == -1) return "";
        startIndex += key.length();
        
        // Skip any whitespace
        while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
            startIndex++;
        }
        
        // Check if value is string (starts with quote)
        if (json.charAt(startIndex) == '"') {
            startIndex++;
            int endIndex = json.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return json.substring(startIndex, endIndex);
            }
        }
        return "";
    }
    
    private static int extractIntValue(String json, String key) {
        String strValue = extractStringValue(json, key);
        if (!strValue.isEmpty()) {
            try {
                return Integer.parseInt(strValue);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        
        // Handle case where value is not in quotes (for numbers)
        int startIndex = json.indexOf(key);
        if (startIndex == -1) return 0;
        startIndex += key.length();
        
        // Skip whitespace
        while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
            startIndex++;
        }
        
        // Find end of number (comma or brace)
        int endIndex = startIndex;
        while (endIndex < json.length() && json.charAt(endIndex) != ',' && json.charAt(endIndex) != '}') {
            endIndex++;
        }
        
        String numberStr = json.substring(startIndex, endIndex).trim();
        try {
            return Integer.parseInt(numberStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private static double extractDoubleValue(String json, String key) {
        int startIndex = json.indexOf(key);
        if (startIndex == -1) return 0.0;
        startIndex += key.length();
        
        // Skip whitespace
        while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
            startIndex++;
        }
        
        // Find end of number (comma or brace)
        int endIndex = startIndex;
        while (endIndex < json.length() && json.charAt(endIndex) != ',' && json.charAt(endIndex) != '}') {
            endIndex++;
        }
        
        String numberStr = json.substring(startIndex, endIndex).trim();
        try {
            return Double.parseDouble(numberStr);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void printHeader() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║        WELCOME TO BANKING SYSTEM      ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private static void pressEnter() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}