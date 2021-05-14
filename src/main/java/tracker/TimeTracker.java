package tracker;

import Communication.CommunicationService;
import gfg.CalculatingScheduler;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class TimeTracker {
    private static Label timeLabel;
    private static ProgressBar progressBar;

    public static void setTimeLabel(Label timeLabel) {
        TimeTracker.timeLabel = timeLabel;
    }

    public static void setProgressBar(ProgressBar progressBar) {
        TimeTracker.progressBar = progressBar;
    }

    public synchronized static void update(double elapsedTime){
        Platform.runLater(()->{
            timeLabel.setVisible(true);
            timeLabel.setText((elapsedTime * CommunicationService.getTaskCount()) / 1000 + " секунд");
            progressBar.setProgress(1.0 - ((double) CommunicationService.getTaskCount() / CalculatingScheduler.SUBTASK_COUNT));
        });
    }
}
