package org.firstinspires.ftc.teamcode.auton.opmodes.doublepixel.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.detectors.BlueElementPositionDetector;
import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.auton.runners.doublepixel.blue.BlueRightDoublePixelAutoPilotRunner;

@Autonomous(name="Autonomous/Blue/Right/DoublePixel/AutoPilot")
public class BlueRightDoublePixelAutoPilot extends LinearOpMode {
    public void runOpMode() {
        new BlueRightDoublePixelAutoPilotRunner()
                .run(this, BlueElementPositionDetector.class, PixelDetectionConstants.PixelPosition.NONE);
    }
}
