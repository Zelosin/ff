package gfg;

import lombok.Getter;
import lombok.Setter;

public class CalculatingScheduler {

    public static final int SUBTASK_COUNT = 20;

    @Setter
    private static int estimatedTaskCount = 0;

    @Getter
    private static double result;

    public static synchronized void reset(){
        result = 0;
    }

    public static synchronized int decAndGet(){
        return --estimatedTaskCount;
    }

    public synchronized static void add(double localResult) {
        result += localResult;
    }
}
