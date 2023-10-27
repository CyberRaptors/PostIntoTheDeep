package org.firstinspires.ftc.teamcode.auton.runners;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;
import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.teleop.IDriveableRobot;

public class RedLeftSinglePixelRunner extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() {
        return bot;
    }

    protected void internalRun(PixelDetectionConstants.PixelPosition pos) {
        drive.followTrajectory(
                drive.trajectoryBuilder(drive.getPoseEstimate()).forward(10).build()
        );

        while (opModeIsActive()) {
            telemetry.addData("Pixel Position", objectDetector.getCurrentFeed());
            telemetry.update();
        }
    }
}
