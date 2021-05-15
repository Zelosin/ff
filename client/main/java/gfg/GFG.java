package gfg;

public class GFG {

    public static double GLOBAL_UPPER_INTERVAL = 1;
    public static double GLOBAL_DOWN_INTERVAL = 0.5;
    public static double GLOBAL_STEP = 0.00000001;

    static double y(double x){
        return Math.pow(Math.E,x) / (x);
    }

    public static double trapezoidal(double a, double b, double h){
        int n = (int)((b - a) / h);
        double s = y(a) + y(b);

        for (int i = 1; i < n; i++)
            s += 2 * y( a + i * h);

        return (h / 2) * s;
    }
}