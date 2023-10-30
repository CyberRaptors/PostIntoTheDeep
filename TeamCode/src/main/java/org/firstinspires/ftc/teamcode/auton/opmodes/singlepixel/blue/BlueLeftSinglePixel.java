package org.firstinspires.ftc.teamcode.auton.opmodes.singlepixel.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.BlueElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.runners.singlepixel.blue.BlueLeftSinglePixelRunner;

@Autonomous(name="Autonomous/Blue/Left/SinglePixel")
public class BlueLeftSinglePixel extends LinearOpMode {
    public void runOpMode() {
        new BlueLeftSinglePixelRunner()
                .run(this, BlueElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
