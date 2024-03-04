package org.firstinspires.ftc.teamcode.auton.opmodes.doublepixel.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.detectors.RedElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.runners.doublepixel.red.RedLeftDoublePixelNoWaitRunner;

@Autonomous(name="Autonomous/Red/Left/DoublePixel NoWait", group="NoWait")
public class RedLeftDoublePixelNoWait extends LinearOpMode {
    public void runOpMode() {
        new RedLeftDoublePixelNoWaitRunner()
                .run(this, RedElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
