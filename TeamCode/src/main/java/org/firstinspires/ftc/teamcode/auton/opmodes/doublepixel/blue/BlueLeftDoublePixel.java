package org.firstinspires.ftc.teamcode.auton.opmodes.doublepixel.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.BlueElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.runners.doublepixel.blue.BlueLeftDoublePixelRunner;
import org.firstinspires.ftc.teamcode.auton.runners.doublepixel.blue.inharmonious.BlueRightDoublePixelInHarmoniousRunnerRPark;

@Autonomous(name="Autonomous/Blue/Left/DoublePixel")
public class BlueLeftDoublePixel extends LinearOpMode {
    public void runOpMode() {
        new BlueLeftDoublePixelRunner()
                .run(this, BlueElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
