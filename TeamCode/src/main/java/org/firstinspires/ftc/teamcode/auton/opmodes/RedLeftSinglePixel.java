package org.firstinspires.ftc.teamcode.auton.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.RedElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.runners.RedLeftSinglePixelRunner;
import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;

@Autonomous(name="Autonomous/Red/Left/SinglePixel")
public class RedLeftSinglePixel extends LinearOpMode {
    public void runOpMode() {
        new RedLeftSinglePixelRunner()
                .run(this, RedElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
