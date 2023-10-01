package org.firstinspires.ftc.teamcode.raptor.auton.runners;

import org.firstinspires.ftc.teamcode.raptor.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.raptor.robot.RaptorRobot;
import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.teleop.IDriveableRobot;

public class BlueLeftSinglePixelRunner extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() {
        return bot;
    }

    protected void internalRun(PixelDetectionConstants.PixelPosition pos) {
        while (opModeIsActive()) {
            telemetry.addData("Pixel Position", pos);
            telemetry.update();
        }
    }
}
