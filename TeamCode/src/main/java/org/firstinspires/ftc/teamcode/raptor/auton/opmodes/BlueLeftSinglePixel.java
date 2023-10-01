package org.firstinspires.ftc.teamcode.raptor.auton.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.raptor.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.raptor.auton.detectors.PrimaryPositionDetector;
import org.firstinspires.ftc.teamcode.raptor.auton.runners.BlueLeftSinglePixelRunner;

@Autonomous(name="Autonomous/Blue/Left/SinglePixel")
public class BlueLeftSinglePixel extends LinearOpMode {
    public void runOpMode() {
        new BlueLeftSinglePixelRunner()
                .run(this, PrimaryPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
