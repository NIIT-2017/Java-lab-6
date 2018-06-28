package Task1and2.phrases;

import Task1and2.client.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class Phrases  extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Label phraseLabel = new Label("нажмите на кнопку \"получить фразу\"");
        phraseLabel.setMaxWidth(300);
        phraseLabel.setWrapText(true);

        Button getPhraseButton = new Button("получить фразу");
        getPhraseButton.setOnAction(event -> {
            Client client = new Client("localhost", 2000);
            Platform.runLater(() -> {
                phraseLabel.setText(client.receivePhrase());
            });
        });


        VBox vBox = new VBox(10.0,getPhraseButton,phraseLabel);


        primaryStage.setScene(new Scene(vBox,300,200));
        primaryStage.show();
    }
}
