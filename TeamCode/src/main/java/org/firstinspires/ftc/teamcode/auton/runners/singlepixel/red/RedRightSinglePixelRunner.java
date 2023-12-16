package org.firstinspires.ftc.teamcode.auton.runners.singlepixel.red;

import static lib8812.common.auton.autopilot.FieldPositions.Autonomous;
import static lib8812.common.auton.autopilot.FieldPositions.BLOCK_LENGTH_IN;
import static lib8812.common.auton.autopilot.FieldPositions.HALF_BLOCK_LENGTH_IN;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.rr.trajectorysequence.TrajectorySequence;
import lib8812.common.robot.IDriveableRobot;


public class RedRightSinglePixelRunner extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() {
        return bot;
    }

    void armDown()
    {
        bot.clawRotate.setPosition(0.74);
        sleep(500);
        bot.arm.setPosition(bot.arm.maxPos-50);
        bot.arm.waitForPosition();
    }

    void armUp()
    {
        bot.arm.setPosition(500);
        bot.arm.waitForPosition();
        bot.arm.setPosition(bot.arm.minPos);
        bot.arm.waitForPosition();
    }

    void dropPurple()
    {
        armDown();
        sleep(1000);
        bot.pixelManager.releaseAutonOneFront();
        sleep(1000);
        armUp();
        sleep(1000);
    }

    protected void internalRun(PixelDetectionConstants.PixelPosition pos) {
        sleep(1000); // see below comment
        pos = objectDetector.getCurrentFeed(); // see if this changes anything

        drive.setPoseEstimate(Autonomous.RED_RIGHT_START);
        drive.followTrajectory(
                drive.trajectoryBuilder(drive.getPoseEstimate())
                        .forward(2) // center self in middle of mat
                        .build()
        );

        TrajectorySequence toTape = null;
        TrajectorySequence toBackdrop = null;

        switch (pos)
        {
            case RIGHT:
                toTape = drive.trajectorySequenceBuilder(Autonomous.RED_RIGHT_START)
                        .forward(HALF_BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(-33))
                        .build();
                toBackdrop = drive.trajectorySequenceBuilder(toTape.end())
                        .turn(Math.toRadians(33))
                        .back(HALF_BLOCK_LENGTH_IN)
                        .strafeRight(BLOCK_LENGTH_IN*2-3)
                        .build();
                break;
            case CENTER:
                toTape = drive.trajectorySequenceBuilder(Autonomous.RED_RIGHT_START)
                        .forward(HALF_BLOCK_LENGTH_IN)
                        .build();
                toBackdrop = drive.trajectorySequenceBuilder(toTape.end())
                        .back(HALF_BLOCK_LENGTH_IN)
                        .strafeRight(BLOCK_LENGTH_IN*2-3)
                        .build();
                break;
            case LEFT:
                toTape = drive.trajectorySequenceBuilder(Autonomous.RED_RIGHT_START)
                        .forward(HALF_BLOCK_LENGTH_IN/2)
                        .turn(Math.toRadians(33))
                        .build();
                toBackdrop = drive.trajectorySequenceBuilder(toTape.end())
                        .turn(Math.toRadians(-33))
                        .back(HALF_BLOCK_LENGTH_IN/2-3)
                        .strafeRight(BLOCK_LENGTH_IN*2)
                        .build();
                break;
        }

        drive.followTrajectorySequence(toTape);
        dropPurple();
        drive.followTrajectorySequence(toBackdrop);
    }
}
