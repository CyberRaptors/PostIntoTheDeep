package org.firstinspires.ftc.teamcode.auton.runners;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;
import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.auton.autopilot.FieldPositions;
import lib8812.common.rr.trajectorysequence.TrajectorySequence;
import lib8812.common.teleop.IDriveableRobot;

public class RedLeftSinglePixelRunner extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() {
        return bot;
    }

    protected void internalRun(PixelDetectionConstants.PixelPosition pos) {
        sleep(1000);
        pos = objectDetector.getCurrentFeed(); // see if this changes anything

        TrajectorySequence program = drive.trajectorySequenceBuilder(FieldPositions.Autonomous.RED_LEFT_START)
                        .forward(0)
                        /* ... */
                        .build();

        drive.followTrajectorySequence(program);
    }
}
