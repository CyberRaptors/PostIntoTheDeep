package org.firstinspires.ftc.teamcode.auton.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.RedElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.runners.RedLeftSinglePixelAutoPilotRunner;
import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;

@Autonomous(name="Autonomous/Red/Left/SinglePixel/AutoPilot")
public class RedLeftSinglePixelAutoPilot extends LinearOpMode {
    public void runOpMode() {
        new RedLeftSinglePixelAutoPilotRunner()
                .run(this, RedElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
