package org.firstinspires.ftc.teamcode.auton.runners;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;
import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.auton.autopilot.PositionalConstants;
import lib8812.common.auton.autopilot.TrajectoryDelegator;
import lib8812.common.auton.autopilot.TrajectoryLists;
import lib8812.common.teleop.IDriveableRobot;

public class RedLeftSinglePixelAutoPilotRunner extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() { return bot; }

    protected void internalRun(PixelDetectionConstants.PixelPosition pos) {
        TrajectoryDelegator delegator = new TrajectoryDelegator(opMode, drive, TrajectoryLists.toRedBackdrop);

        // drop first pixel on tape

        drive.followTrajectoryAsync(TrajectoryLists.toRedBackdrop[0]);

        while (!delegator.isAtTarget(PositionalConstants.redBackdrop)) {
            if (delegator.shouldChangeTrajectory()) {
                drive.breakFollowing();
                drive.followTrajectorySequenceAsync(
                        delegator.getNewTrajectoryPath()
                );
            }
        }

        // drop other pixel
    }
}