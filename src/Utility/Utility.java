package Utility;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.concurrent.TimeUnit;

public class Utility {

    public static void alert(Alert.AlertType type, String message ){
        Alert alert = new Alert(type,message);
        alert.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    Platform.runLater(()->{
                        if(alert.isShowing()) alert.close();
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
