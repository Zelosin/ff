package logger;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class Logger {
    private static ListView<String> dLogger;

    public static void setdLogger(ListView<String> dLogger) {
        Logger.dLogger = dLogger;
    }

    public static void log(String text) {
        Platform.runLater(() -> {
            dLogger.getItems().add(text);
            dLogger.refresh();
        });
    }
}
