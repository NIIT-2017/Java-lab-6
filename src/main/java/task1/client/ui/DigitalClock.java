package task1.client.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DigitalClock extends StackPane {

    private Canvas canvas = new Canvas();

    private GraphicsContext gc = canvas.getGraphicsContext2D();

    private Text text = new Text("");

    private StringProperty timeNow = new SimpleStringProperty();

    public DigitalClock() {
        this.setWidth(100);
        this.setHeight(50);
        canvas.setWidth(this.getWidth());
        canvas.setHeight(this.getHeight());
        text.setFill(Color.WHITE);
        text.setFont(Font.font("null", FontWeight.BOLD, 15));
        timeNow.setValue("00:00:00");
        text.textProperty().bind(timeNow);

        getChildren().addAll(canvas, text);

        paintClock();
    }

    public void setTime(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        //Здесь дописать дату через calendar.get
        timeNow.setValue(zero(calendar.get(Calendar.HOUR)) + ":" + zero(calendar.get(Calendar.MINUTE)) + ":" + zero(calendar.get(Calendar.SECOND)) + ":" + zero(calendar.get(Calendar.YEAR)));
    }

    private void paintClock() {
        gc.clearRect(0, 0, getWidth(), getHeight());

        gc.setFill(Color.FIREBRICK);
        gc.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
    }

    private String zero(int number) {
        return (number < 10) ? "0" + number : "" + number;
    }

    public void setHeight(double height) {
        super.setHeight(height);
    }

    public void setWidth(double width) {
        super.setWidth(width);
    }

}