package org.firstinspires.ftc.teamcode.auton.opmodes.singlepixel.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.detectors.RedElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.runners.singlepixel.red.RedRightSinglePixelRunner;

@Autonomous(name="Autonomous/Red/Right/SinglePixel")
public class RedRightSinglePixel extends LinearOpMode {
    public void runOpMode() {
        new RedRightSinglePixelRunner()
                .run(this, RedElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
