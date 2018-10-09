package gui;

import client.Client;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.Scanner;

public class Main extends Application{

    private Scanner in;
    private PrintWriter out;
    private Label aphorism;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Client client = new Client();
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: black");
        in = client.getIn();
        out = client.getOut();
        aphorism = new Label();
        aphorism.setStyle(" -fx-font-size: 15; -fx-max-width: 400; -fx-wrap-text: true; -fx-text-fill: white");
        Button button = new Button("Click me");
        button.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-text-fill: white;" +
                " -fx-border-radius: 5; -fx-pref-height: 40; -fx-pref-width: 80; -fx-font-size: 13");
        button.setOnAction(event ->
        {
            aphorism.setText(in.nextLine());
            aphorism.requestLayout();
        });
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(button);
        borderPane.setBottom(stackPane);
        borderPane.setPadding(new Insets(40));
        borderPane.setCenter(aphorism);
        Scene scene = new Scene(borderPane, 500,200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Get Aphorism");
        primaryStage.show();
        primaryStage.setOnCloseRequest(e->
                {
                    out.close();
                    in.close();
                    try
                    {
                        client.getSocket().close();
                    } catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
        );
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
