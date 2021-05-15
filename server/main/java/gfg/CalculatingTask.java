package gfg;

import com.google.gson.Gson;
import logger.Logger;
import lombok.*;

@RequiredArgsConstructor
public class CalculatingTask implements Runnable{

    @Setter
    @Getter
    private int status;

    @NonNull
    @Getter
    private final double upperInterval;

    @NonNull
    @Getter
    private final double downInterval;

    @Setter
    private double elapsedTime;

    @NonNull
    @Getter
    private int loadPercent;

    private final CalculatingScheduler scheduler;

    @Setter
    private double localResult;

    public String serialize(){
        return new Gson().toJson(this);
    }

    public static CalculatingTask deserialize(String data){
        return new Gson().fromJson(data, CalculatingTask.class);
    }

    @SneakyThrows
    @Override
    public void run() {
        Logger.log(String.format("Поток начал свое выполнение от: %f до: %f%n", upperInterval, downInterval));
        var timeStart = System.currentTimeMillis();
        localResult = GFG.trapezoidal(upperInterval, downInterval, GFG.GLOBAL_STEP);
        var timeEnd = System.currentTimeMillis();
        var workTime = timeEnd - timeStart;
        Thread.sleep((workTime * loadPercent) - workTime);
        scheduler.add(localResult);
    }
}
