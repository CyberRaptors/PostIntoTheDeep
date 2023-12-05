package org.firstinspires.ftc.teamcode.auton.opmodes.doublepixel.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.BlueElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.runners.doublepixel.blue.BlueRightDoublePixelInHarmoniousRunner;

@Autonomous(name="Autonomous/Blue/Right/DoublePixel [InHarmonious]")
public class BlueRightDoublePixelInHarmonious extends LinearOpMode {
    public void runOpMode() {
        new BlueRightDoublePixelInHarmoniousRunner()
                .run(this, BlueElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
