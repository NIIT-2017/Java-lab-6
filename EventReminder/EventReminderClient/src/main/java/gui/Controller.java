package gui;

import client.Client;
//import server.Message;
import shared.Message;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;


public class Controller implements Initializable
{
    @FXML
    private BorderPane pane;

    @FXML
    private Label event;
    private boolean flag = true;
    private boolean flag2 = true;
    private Message msg;
    private Stage stage;
    private Timer notificationTimer = new Timer();
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Client client;

    public Controller() throws IOException {
    }
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        stage = Main.getStage();
        pane.setStyle("-fx-background-color: #ff99ff");
        event.setStyle("-fx-font-size: 20;-fx-text-alignment: center; -fx-text-fill: blue");

        client = new Client();
        output = client.getOutput();
        input = client.getInput();

        try {
           output.writeObject(new Message("id1","hi"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        showMessage(input, output);

        Platform.setImplicitExit(false);
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);
    }

    private void addAppToTray() {
        try {

            java.awt.Toolkit.getDefaultToolkit();

            if (!SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }
            SystemTray tray = SystemTray.getSystemTray();

            InputStream inputStream = getClass().getResourceAsStream("/bell.png");
            TrayIcon trayIcon = new TrayIcon(ImageIO.read(inputStream),"Event Reminder");
            trayIcon.setImageAutoSize(true);

            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            MenuItem openItem = new MenuItem("Event Reminder");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(event -> {
                notificationTimer.cancel();
                Platform.exit();
                tray.remove(trayIcon);
                try {
                    input.close();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            final PopupMenu popup = new PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
    }

    private void showMessage(ObjectInputStream finalInput, ObjectOutputStream finalOutput){
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (flag){
                    msg = (Message)finalInput.readObject();
                    if (msg.getMessage().equals("ping")){
                        finalOutput.writeObject(new Message("id1","ping"));
                        finalOutput.flush();
                    }
                    else if (!msg.getMessage().equals("ping")&&!msg.getMessage().equals("")){
                        updateMessage(msg.getMessage());
                    }
                }
                return null;
            }
        };
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
        event.textProperty().bind(task.messageProperty());
        event.textProperty().addListener(e->{
            stage.show();
        });
    }

    private void showStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }
}