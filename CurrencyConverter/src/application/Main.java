package application;
	
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;

import java.util.Currency;
import java.util.Set;
import java.util.Locale;

public class Main extends Application {
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
		launch(args);
	}
}
