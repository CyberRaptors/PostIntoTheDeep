package org.firstinspires.ftc.teamcode.auton.opmodes.doublepixel.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.detectors.RedElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.runners.doublepixel.red.RedLeftDoublePixelInHarmoniousRunner;

@Autonomous(name="Autonomous/Red/Left/DoublePixel [InHarmonious]")
public class RedLeftDoublePixelInHarmonious extends LinearOpMode {
    public void runOpMode() {
        new RedLeftDoublePixelInHarmoniousRunner()
                .run(this, RedElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
