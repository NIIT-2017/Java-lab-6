package task1.client.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/task1/fxml/app.fxml";
        FXMLLoader loader = new FXMLLoader();
        AnchorPane root = loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Lab6. Task 1");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
