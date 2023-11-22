package lib8812.common.teleop;


public class TeleOpUtils {
    public static final double DEFAULT_FINE_TUNE_THRESH = 0.2;

    public static boolean isBetweenInclusive(double val, double low, double high)
    {
        return val >= low && val <= high;
    }

    public static double fineTuneInput(double input, double thresh)
    {
        if (isBetweenInclusive(input, 1-thresh, 1)) return 1;
        if (isBetweenInclusive(input, 0, 0+thresh)) return 0;

        return input;
    }
}
