package Finance;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FinPalApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file designed in Scene Builder
        Parent root = FXMLLoader.load(getClass().getResource("FinPalGUI.fxml"));

        // Create a scene with the loaded FXML
        Scene scene = new Scene(root);

        // Set the scene on the primary stage and configure the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("FinPal - Your Personal Finance Manager");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
