package lib8812.common.builtindetectors;

import lib8812.common.auton.IModelLabel;
import lib8812.common.auton.IObjectDetectionConstants;

public class RobotDetectionConstants implements IObjectDetectionConstants {
    public enum RobotThreat implements IModelLabel {
        URGENT, DISTANT, NONE, LEFT_SIDE, RIGHT_SIDE
    }

    public static final String[] LABELS = {
            "ftcrobot"
    };

    public static final String PRIMARY_TFOD_MODEL_FILE = "TestRobotModel.tflite";
}
