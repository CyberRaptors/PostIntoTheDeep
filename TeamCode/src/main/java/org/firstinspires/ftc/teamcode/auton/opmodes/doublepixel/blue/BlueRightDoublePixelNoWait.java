package org.firstinspires.ftc.teamcode.auton.opmodes.doublepixel.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.BlueElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.runners.doublepixel.blue.BlueRightDoublePixelNoWaitRunner;

@Autonomous(name="Autonomous/Blue/Right/DoublePixel NoWait", group="NoWait")
public class BlueRightDoublePixelNoWait extends LinearOpMode {
    public void runOpMode() {
        new BlueRightDoublePixelNoWaitRunner()
                .run(this, BlueElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
