package gui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*InputStream input = Main.class.getResourceAsStream("/fxml/main.fxml");
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(input);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setController(new MainController());
        Parent root = loader.load();*/
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        primaryStage.setTitle("Конвертер валют");
        primaryStage.setMinWidth(670);
        primaryStage.setMinHeight(500);
        Scene scene = new Scene(root, 670, 500);
        scene.getStylesheets().addAll(this.getClass().getResource("/css/main.css").toString());
        primaryStage.setScene(scene);
        primaryStage.requestFocus();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
