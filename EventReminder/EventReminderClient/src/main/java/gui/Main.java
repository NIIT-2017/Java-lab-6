package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import java.awt.*;


public class Main extends Application {

    private static Stage stage;
    private static Scene scene;

    public static Stage getStage(){
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("Event Reminder");
        scene = new Scene(root, 350, 200);
        primaryStage.setScene(scene);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int taskBarHeight = screenSize.height - winSize.height;
        primaryStage.setX(screenSize.getWidth()-scene.getWidth());
        primaryStage.setY(screenSize.getHeight()-scene.getHeight()-taskBarHeight*2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
