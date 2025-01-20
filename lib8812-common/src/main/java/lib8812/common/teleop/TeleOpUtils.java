package lib8812.common.teleop;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TeleOpUtils {
    public static final double DEFAULT_FINE_TUNE_THRESH = 0.2;
    public static final double DEFAULT_LINEAR_INPUT_SCALE = 2.5;

    final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    public static boolean isBetweenInclusive(double val, double low, double high)
    {
        return val >= low && val <= high;
    }

    public static double fineTuneInput(double input, double thresh)
    {
        if (isBetweenInclusive(input, 1-thresh, 1)) return 1;
        if (isBetweenInclusive(input, 0-thresh, 0+thresh)) return 0;
        if (isBetweenInclusive(input, -1, -1+thresh)) return -1;

        return input;
    }

    public static double quadraticallyScaleInput(double input, double linearScale) {
        linearScale = Math.abs(linearScale);

        double sign = Math.signum(input);

        double linearlyScaled = input*linearScale;

		return sign*   Math.pow(linearlyScaled, 2);
    }

    public static double quadraticallyScaleInput(double input) {
        return quadraticallyScaleInput(input, DEFAULT_LINEAR_INPUT_SCALE);
    }

    public static double fineTuneInput(double input) { return fineTuneInput(input, DEFAULT_FINE_TUNE_THRESH); }

    public static void setTimeout(Runnable runnable, long delay) {
        scheduler.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }
}
