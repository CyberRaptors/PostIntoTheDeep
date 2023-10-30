package org.firstinspires.ftc.teamcode.auton.opmodes.doublepixel.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.detectors.RedElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.runners.doublepixel.red.RedRightDoublePixelRunner;

@Autonomous(name="Autonomous/Red/Right/DoublePixel")
public class RedRightDoublePixel extends LinearOpMode {
    public void runOpMode() {
        new RedRightDoublePixelRunner()
                .run(this, RedElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
