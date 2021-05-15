package gfg;


import logger.Logger;
import lombok.Getter;
import lombok.var;

import java.util.ArrayList;
import java.util.List;

public class CalculatingScheduler {

    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    public static int loadPercent = 20;

    @Getter
    private double result = 0;

    private final List<Thread> processingThreads = new ArrayList<>();

    public CalculatingScheduler init(CalculatingTask task) {
        Logger.log("Количество потоков: " + THREAD_COUNT);
        double calculatedPeriod = (task.getUpperInterval() - task.getDownInterval()) / THREAD_COUNT;
        double taskInterval = task.getDownInterval();
        for (var i = 0; i < THREAD_COUNT; i++) {
            processingThreads.add(new Thread(new CalculatingTask(taskInterval, taskInterval + calculatedPeriod, task.getLoadPercent(), this)));
            taskInterval = taskInterval + calculatedPeriod;
        }
        return this;
    }

    public CalculatingScheduler start() {
        processingThreads.forEach(Thread::start);
        return this;
    }

    public synchronized void add(double localResult) {
        result += localResult;
    }

    public double join() {
        processingThreads.forEach(processingThread -> {
            try {
                processingThread.join();
            } catch (InterruptedException ignore) {
            }
        });
        Logger.log("Результат вычислений :" + result);
        return result;
    }
}
