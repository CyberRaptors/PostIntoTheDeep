package org.firstinspires.ftc.teamcode.auton.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.PrimaryPositionDetector;
import org.firstinspires.ftc.teamcode.auton.runners.RedLeftSinglePixelRunner;
import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.runners.RedParkRunner;

@Autonomous(name="Autonomous/Red/Right/Park")
public class RedPark extends LinearOpMode {
    public void runOpMode() {
        new RedParkRunner()
                .run(this, PrimaryPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
