package org.firstinspires.ftc.teamcode.auton.opmodes.doublepixel.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.RedElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.runners.doublepixel.red.RedLeftDoublePixelHarmoniousRunner;
import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;

@Autonomous(name="Autonomous/Red/Left/DoublePixel [Harmonious]")
public class RedLeftDoublePixelHarmonious extends LinearOpMode {
    public void runOpMode() {
        new RedLeftDoublePixelHarmoniousRunner()
                .run(this, RedElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
