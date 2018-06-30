package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller
{
    @FXML
    private TextField tfDateTime;
    @FXML
    private TextField tfStatus;

    @FXML
    private Button btnPower;
    @FXML
    private Button btnSynchDTime;

    @FXML
    public void initialize() {
        setTFDateTime("00.00.0000 00:00:00");
        setTFStatus("Status: OFF");
        setBtnPower("ON");
        setBtnSynchDTime("For synchronize");
        setDisableSynch(true);
        myThread = null;
    }

    @FXML
    public void onClick_btnPower() {
        if (myThread == null) {
            if (synchDTimeWithServer()) {
                setBtnPower("OFF");
                setBtnSynchDTime("To synchronize");
                setDisableSynch(false);
            }
        }
        else {
            myThreadStop();
            initialize();
        }
    }

    private void myThreadStop() {
        myThread.interrupt();
        try {
            myThread.join();
        }
        catch(InterruptedException ie) {
        }
    }

    @FXML
    public void onClick_btnSynchDTime() {
        synchDTimeWithServer();
    }

    private boolean synchDTimeWithServer() {
        if (myThread != null) {
            myThreadStop();
            myThread = null;
        }
        String response = DTimeClient.requestToServer("getDateTimeMoscow");
        Pattern p = Pattern.compile("^.*([0-9]{2}[.][0-9]{2}[.][0-9]{4}[ ][0-9]{2}[:][0-9]{2}[:][0-9]{2}).*$");
        Matcher m = p.matcher(response);
        if (m.matches()) {
            String dTime = m.group(1);
            setDateTime(LocalDateTime.parse(dTime, dateTimeForm));
            myThread = new MyThread();
            myThread.start();
            setTFStatus("Synchronized: " + dTime);
            return true;
        }
        else {
            setTFStatus(response);
        }
        return false;
    }

    private void setTFDateTime(String str) {
        tfDateTime.setText(str);
    }
    private void setTFStatus(String str) {
        tfStatus.setText(str);
    }
    private void setBtnPower(String str) {
        btnPower.setText(str);
    }
    private void setBtnSynchDTime(String str) {
        btnSynchDTime.setText(str);
    }
    private void setDisableSynch(boolean b) {
        btnSynchDTime.setDisable(b);
    }
    private void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    private LocalDateTime dateTime;
    private DateTimeFormatter dateTimeForm = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private MyThread myThread;

    private class MyThread extends Thread
    {
        public void run() {
           try {
                while(!exitStat && !Thread.currentThread().isInterrupted()) {
                    setTFDateTime(dateTime.format(dateTimeForm));
                    setDateTime(dateTime.plusSeconds(1));
                    this.sleep(1000);
                    long diffSec = ChronoUnit.SECONDS.between(dateTime, LocalDateTime.now());
                    if (diffSec >= 1) {
                        setDateTime(dateTime.plusSeconds(diffSec));
                    }
                }
            }
            catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
    private static volatile boolean exitStat = false;
    @FXML
    public static void setExitStat(boolean exitStat) {
        Controller.exitStat = exitStat;
    }
}
