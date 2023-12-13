package org.firstinspires.ftc.teamcode.auton.runners.singlepixel.blue;

import static lib8812.common.auton.autopilot.FieldPositions.Autonomous;
import static lib8812.common.auton.autopilot.FieldPositions.BLOCK_LENGTH_IN;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.rr.trajectorysequence.TrajectorySequence;
import lib8812.common.robot.IDriveableRobot;


public class BlueLeftSinglePixelRunner extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() {
        return bot;
    }

    void armDown()
    {
        bot.clawRotate.setPosition(0.74);
        sleep(500);
        bot.arm.setPosition(bot.arm.maxPos-10);
        bot.arm.waitForPosition();
        sleep(300);
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

        drive.setPoseEstimate(Autonomous.BLUE_LEFT_START);

        TrajectorySequence toTape = null;
        TrajectorySequence toBackdrop = null;

        switch (pos)
        {
            case RIGHT:
                toTape = drive.trajectorySequenceBuilder(Autonomous.BLUE_LEFT_START)
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(45))
                        .build();
                toBackdrop = drive.trajectorySequenceBuilder(toTape.end())
                        .turn(Math.toRadians(-45))
                        .back(BLOCK_LENGTH_IN)
                        .strafeLeft(BLOCK_LENGTH_IN*2)
                        .build();
                break;
            case CENTER:
                toTape = drive.trajectorySequenceBuilder(Autonomous.BLUE_LEFT_START)
                        .forward(BLOCK_LENGTH_IN)
                        .build();
                toBackdrop = drive.trajectorySequenceBuilder(toTape.end())
                        .back(BLOCK_LENGTH_IN)
                        .strafeLeft(BLOCK_LENGTH_IN*2)
                        .build();
                break;
            case LEFT:
                toTape = drive.trajectorySequenceBuilder(Autonomous.BLUE_LEFT_START)
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(-45))
                        .build();
                toBackdrop = drive.trajectorySequenceBuilder(toTape.end())
                        .turn(Math.toRadians(45))
                        .back(BLOCK_LENGTH_IN)
                        .strafeLeft(BLOCK_LENGTH_IN*2)
                        .build();
                break;
        }



        drive.followTrajectorySequence(toTape);
        dropPurple();
        drive.followTrajectorySequence(toBackdrop);
    }
}
