package org.firstinspires.ftc.teamcode.auton.opmodes.doublepixel.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.BlueElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.runners.doublepixel.blue.BlueRightDoublePixelHarmoniousRunner;

@Autonomous(name="Autonomous/Blue/Right/DoublePixel [Harmonious]")
public class BlueRightDoublePixelHarmonious extends LinearOpMode {
    public void runOpMode() {
        new BlueRightDoublePixelHarmoniousRunner()
                .run(this, BlueElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
