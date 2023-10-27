package org.firstinspires.ftc.teamcode.auton.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.PrimaryPositionDetector;
import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.runners.BlueParkRunner;

@Autonomous(name="Autonomous/Blue/Left/Park")
public class BluePark extends LinearOpMode {
    public void runOpMode() {
        new BlueParkRunner()
                .run(this, PrimaryPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
