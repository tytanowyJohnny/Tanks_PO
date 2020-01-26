package Tanks;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class timerController implements Initializable {


    private long startTimeInMilis = 60000; // 1 minute

    @FXML
    private Label clockLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Thread clock = new Thread(() -> {

            while(startTimeInMilis > 0) {

                try {

                    TimeUnit.MILLISECONDS.sleep(1);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }

                startTimeInMilis -= 1;

                //long elapsedSeconds = starTimeInMilis / 1000;
                int elapsedMiliseconds = (int) startTimeInMilis % 1000;
                int elapsedSeconds = (int) startTimeInMilis / 1000;

                Platform.runLater(() -> {

                    clockLabel.setText(elapsedSeconds + ":" + elapsedMiliseconds);

                });

            }

            Platform.runLater(() -> {

                // Close window
                Stage currentWindow = (Stage) clockLabel.getScene().getWindow();
                currentWindow.close();

            });


        });

        clock.setDaemon(true);
        clock.start();

    }


}
