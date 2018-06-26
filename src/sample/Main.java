package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("DateTime");
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.setMinWidth(718);
        primaryStage.setMinHeight(644);
        primaryStage.setMaxWidth(718);
        primaryStage.setMaxHeight(644);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        Controller.setExitStat(true);
    }
}
