package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Load initial (login) view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/LoginView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        // Optional stylesheet
        var css = getClass().getResource("/Styles/styles.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setTitle("Public Utility Billing System");
        stage.setScene(scene);
        stage.setWidth(1050);
        stage.setHeight(680);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}