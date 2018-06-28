package Task3.client;

import Task3.conection.MyConnection;
import Task3.conection.MyConnectionListener;
import com.sun.corba.se.impl.orb.ParserTable;
import com.sun.xml.internal.ws.util.InjectionPlan;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application implements MyConnectionListener {
    private final TextArea log = new TextArea();
    private MyConnection connection;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //init main scene
        AnchorPane.setRightAnchor(log,5.0);
        AnchorPane.setBottomAnchor(log,20.0);
        AnchorPane.setLeftAnchor(log,5.0);
        AnchorPane.setTopAnchor(log,20.0);
        //field host name
        TextField hostTextField = new TextField("localhost");
        hostTextField.setPrefWidth(110);
        //field port number
        TextField portTextField = new TextField("2021");
        portTextField.setPrefWidth(60);
        //button for connect to server
        Button connect = new Button("connect to server");
        connect.setOnAction(event -> {
            try {
                connection = new MyConnection(hostTextField.getText(),Integer.parseInt(portTextField.getText()),this);
            } catch (IOException e) {
                log.appendText("Exception connect to server: "+e+"\n");
            }
        });
        //layout
        HBox hBox = new HBox(hostTextField, portTextField, connect);
        hBox.setSpacing(10);
        //lauout
        VBox vBox = new VBox(hBox, log);
        AnchorPane.setRightAnchor(vBox,5.0);
        AnchorPane.setBottomAnchor(vBox,5.0);
        AnchorPane.setLeftAnchor(vBox,5.0);
        AnchorPane.setTopAnchor(vBox,5.0);
        //main Stage
        AnchorPane root = new AnchorPane(vBox);
        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(root,350,200));
        //exit from program
        primaryStage.setOnCloseRequest(event -> connection.sendString(MyConnection.dis));
        primaryStage.show();
    }

    //events
    //all events print into log area
    @Override
    public void onConnect(MyConnection connection) {
        Platform.runLater(() -> log.appendText("connected to server\n"));
    }

    @Override
    public void onReceive(MyConnection connection, String message) {
        Platform.runLater(() -> log.appendText(message+"\n"));
    }

    @Override
    public void onDisconnect(MyConnection connection) {
        Platform.runLater(() -> log.appendText("disconnected from server\n"));
    }

    @Override
    public void onException(MyConnection connection, Exception e) {
        Platform.runLater(() -> log.appendText("Exception connection: "+e+"\n"));
    }
}
