import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyConverterAPI 
{

    // The base URL for the currency API
    private static final String API_BASE_URL = "https://api.frankfurter.app/latest";

    // Create an HttpClient. We can reuse this for all requests.
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("===== Welcome to the Real-Time Currency Converter =====");
        System.out.println(" (Powered by Frankfurter.app)");

        // Main application loop
        while (true) 
        {
            // 1. Get Base Currency
            String baseCurrency = readCurrency(scanner, "Enter the base currency (e.g., USD, EUR, JPY): ");

            // 2. Get Target Currency
            String targetCurrency = readCurrency(scanner, "Enter the target currency (e.g., USD, EUR, JPY): ");

            // 3. Get Amount
            double amount = readDouble(scanner, "Enter the amount in " + baseCurrency + ": ");

            try 
            {
                // 4. Fetch the exchange rate
                System.out.println("\nFetching real-time exchange rate...");
                double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);

                if (exchangeRate == -1.0) 
                {
                    System.out.println("Sorry, couldn't find a rate for one of those currencies.");
                    System.out.println("Please check the 3-letter codes and try again.");
                } 
                else 
                {
                    // 5. Calculate and Display Result
                    double convertedAmount = amount * exchangeRate;
                    
                    System.out.println("\n--- Result ---");
                    System.out.printf("Rate: 1 %s = %.4f %s\n", baseCurrency, exchangeRate, targetCurrency);
                    System.out.printf("Amount: %.2f %s = %.2f %s\n",
                                      amount, baseCurrency,
                                      convertedAmount, targetCurrency);
                    System.out.println("--------------");
                }

            } 
            catch (IOException | InterruptedException e) 
            {
                System.err.println("Error: Could not connect to the currency service.");
                System.err.println("Please check your internet connection and try again.");
                // e.printStackTrace(); // Uncomment for debugging
            }

            // 6. Ask to repeat
            System.out.print("\nDo you want to perform another conversion? (yes/no): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (!choice.equals("yes") && !choice.equals("y"))
            {
                break; // Exit the while loop
            }
            System.out.println(); // Add a space for the next loop
        }

        System.out.println("Thank you for using the Currency Converter! Goodbye.");
        scanner.close();
    }

    /**
     * Fetches the exchange rate from the API.
     *
     * @param baseCurrency   The 3-letter code for the base currency.
     * @param targetCurrency The 3-letter code for the target currency.
     * @return The exchange rate (1 base = X target), or -1.0 if not found.
     */
    private static double fetchExchangeRate(String baseCurrency, String targetCurrency) throws IOException, InterruptedException 
    {    
        // Build the request URL string
         String urlString = String.format("%s?from=%s&to=%s", API_BASE_URL, baseCurrency, targetCurrency);
        // Create the HTTP request
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlString)).build();
        // Send the request and get the response as a String
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // Check if the request was successful
        if (response.statusCode() != 200)
        {
            throw new IOException("API request failed with status code: " + response.statusCode());
        }
        String jsonResponse = response.body();
        
        // Parse the rate from the JSON response
        return parseRateFromJson(jsonResponse, targetCurrency);
    }

    /**
     * A simple, custom parser to find the rate in the JSON response
     * without needing an external library.
     *
     * Example JSON: {"amount":1.0,"base":"USD","date":"...","rates":{"JPY":149.62}}
     *
     * @param jsonResponse   The full JSON string from the API.
     * @param targetCurrency The currency to find (e.g., "JPY").
     * @return The exchange rate as a double, or -1.0 if not found.
     */
    private static double parseRateFromJson(String jsonResponse, String targetCurrency) 
    {
        try
        {
            // This Regex finds the target currency and captures its rate (a number)
            // It looks for something like "JPY":149.62
            Pattern pattern = Pattern.compile("\"" + targetCurrency + "\":(\\d+(\\.\\d+)?)");
            Matcher matcher = pattern.matcher(jsonResponse);

            if (matcher.find()) 
            {
                // Group 1 is the captured number (e.g., "149.62")
                String rateString = matcher.group(1);
                return Double.parseDouble(rateString);
            } 
            else 
            
            {
                // Rate not found in the response
                return -1.0;
            }
        } 
        catch (Exception e) 
        {
            System.err.println("Error parsing API response.");
            return -1.0;
        }
    }

    // --- Input Validation Helper Methods ---

    /**
     * Reads a valid, 3-letter currency code from the user.
     */
    private static String readCurrency(Scanner scanner, String prompt) 
    {
        while (true) 
        {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            
            // Basic validation: must be 3 letters
            if (input.length() == 3 && input.matches("[A-Z]+")) 
            {
                return input;
            } 
            else 
            {
                System.out.println("Invalid format. Please enter a 3-letter currency code (e.g., USD).");
            }
        }
    }

    /**
     * Reads a valid, positive double (number) from the user.
     */
    private static double readDouble(Scanner scanner, String prompt) 
    {
        while (true) 
        {
            try 
            {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline character

                if (value < 0) 
                {
                    System.out.println("Amount cannot be negative. Please try again.");
                } 
                else 
                {
                    return value;
                }
            } 
            catch (InputMismatchException e) 
            {
                System.out.println("Invalid input. Please enter a number (e.g., 100 or 50.75).");
                scanner.nextLine(); // Clear the bad input
            }
        }
    }
}