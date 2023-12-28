package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONObject; 
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;





public class Main extends Application {
	

	
	 public static BigDecimal convertCurrency(BigDecimal amount, String sourceCurrency, String targetCurrency) throws IOException, ParseException {
	        // Assume a simple exchange rate for demonstration purposes
	        double exchangeRate = getExchangeRate(sourceCurrency, targetCurrency);

	        // Perform the currency conversion
	        BigDecimal convertedAmount = amount.multiply(BigDecimal.valueOf(exchangeRate));

	        // Round the result to two decimal places
	        convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_UP);
	        
	        return convertedAmount;
	    }

	    // Function to get the exchange rate between two currencies
	    private static double getExchangeRate(String from_currency, String to_currency) throws IOException, ParseException {
	        // You can implement a mechanism to retrieve the real exchange rate from an external source
	        // For simplicity, using a hardcoded exchange rate in this example
	    	URL url = new URL("http://api.exchangeratesapi.io/v1/latest"
	    			+ "?access_key=5d39950c42f281f85d76b7e8094a1e81"
	    			+ "&base=EUR"
	    			+ "&symbols=" + from_currency.split(" ")[0] + "," + to_currency.split(" ")[0]);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Check if connect is made
            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                //Close the scanner
                scanner.close();


                //JSON simple library Setup with Maven is used to convert strings to JSON
                JSONParser parse = new JSONParser();
                JSONObject dataObject = (JSONObject)parse.parse(String.valueOf(informationString));

                //Get the first JSON object in the JSON array
                System.out.println(dataObject);
                JSONObject res = (JSONObject) dataObject.get("rates");
                double res1 = (double) res.get(from_currency.split(" ")[0]);
                
                JSONObject res2 = (JSONObject) dataObject.get("rates");
                double res12 = (double) res2.get(to_currency.split(" ")[0]);
                return res12/res1;
	    }
	    }

	@Override
	public void start(Stage primaryStage) {
		try {

			// Create root
			StackPane root = new StackPane();
			
			// Get the set of available currencies
	        Set<Currency> availableCurrencies = Currency.getAvailableCurrencies();

	        // Create an ObservableList to hold the currency codes
	        ObservableList<String> CurrencyNames = FXCollections.observableArrayList();
	        
	        for (Currency currency : availableCurrencies) {
	            CurrencyNames.add(currency.getCurrencyCode() + " - " + currency.getDisplayName(Locale.getDefault()));
	        }
	        FXCollections.sort(CurrencyNames);

	        
	        ComboBox<String> comboBox = new ComboBox<>(CurrencyNames);
	        comboBox.setPromptText("Currency 1");
	        TextField textField = new TextField();
	        HBox hbox1 = new HBox(comboBox, textField);
	        
	        // Create second Currecy  
	        ComboBox<String> comboBox2 = new ComboBox<>(CurrencyNames);
	        comboBox2.setPromptText("Currency 2");
	        TextField textField2 = new TextField();
	        HBox hbox2 = new HBox(comboBox2, textField2);
	       
	        
	        VBox vbox = new VBox(hbox1, hbox2);
	        root.getChildren().add(vbox);
			
	       
	        
	        Button button = new Button("Submit");

	        // Event handler for the button click
	        button.setOnAction(event -> {
	            String currency1 = comboBox.getValue();
	            String currency2 = comboBox2.getValue();
	            BigDecimal amount = new BigDecimal(textField.getText());
	            String result = null;
				try {
					result = convertCurrency(amount,currency1,currency2).toString();
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            textField2.setText(result);
	        });
	        root.getChildren().add(button);
			
			// Create the scene and set it on the stage
			Scene scene = new Scene(root,600,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
	        
			
			// Set the stage title
	        primaryStage.setTitle("Label and TextField Example");

	        // Show the stage
	        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
        // Example usage
        
		launch(args);
	}
}
