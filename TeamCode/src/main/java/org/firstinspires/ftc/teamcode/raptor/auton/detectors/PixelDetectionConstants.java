package org.firstinspires.ftc.teamcode.raptor.auton.detectors;

import lib8812.common.auton.IModelLabel;
import lib8812.common.auton.IObjectDetectionConstants;

public class PixelDetectionConstants implements IObjectDetectionConstants {
    public enum PixelPosition implements IModelLabel {
        LEFT, RIGHT, CENTER, NONE
    }

    public static final String[] LABELS = {
            "left",
            "center",
            "right"
    };

    public static final String PRIMARY_TFOD_MODEL_FILE = "TestModel.tflite";
}
