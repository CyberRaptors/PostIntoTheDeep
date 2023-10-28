package org.firstinspires.ftc.teamcode.auton.runners;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;
import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.auton.autopilot.FieldPositions;
import lib8812.common.auton.autopilot.TrajectoryDelegator;
import lib8812.common.auton.autopilot.TrajectoryLists;
import lib8812.common.teleop.IDriveableRobot;

public class RedLeftSinglePixelAutoPilotRunner extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() { return bot; }

    protected void internalRun(PixelDetectionConstants.PixelPosition pos) {
        TrajectoryDelegator delegator = new TrajectoryDelegator(opMode, drive, TrajectoryLists.FromRedLeft.toRedBackdrop, FieldPositions.InFrontOf.RED_BACKDROP);


        // drop first pixel on tape, then go back to starting pos

        delegator.pathFindToTarget();

        // drop other pixel
    }
}