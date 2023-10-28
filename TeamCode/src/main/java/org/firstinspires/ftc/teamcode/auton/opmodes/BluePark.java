package org.firstinspires.ftc.teamcode.auton.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.BlueElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.detectors.RedElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.runners.BlueParkRunner;

@Autonomous(name="Autonomous/Blue/Left/Park")
public class BluePark extends LinearOpMode {
    public void runOpMode() {
        new BlueParkRunner()
                .run(this, BlueElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
