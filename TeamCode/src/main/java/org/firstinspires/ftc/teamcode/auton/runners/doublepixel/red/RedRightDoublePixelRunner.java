package org.firstinspires.ftc.teamcode.auton.runners.doublepixel.red;

import static lib8812.common.auton.autopilot.FieldPositions.Autonomous;
import static lib8812.common.auton.autopilot.FieldPositions.BLOCK_LENGTH_IN;
import static lib8812.common.auton.autopilot.FieldPositions.HALF_BLOCK_LENGTH_IN;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.auton.autopilot.TrajectoryLists;
import lib8812.common.rr.trajectorysequence.TrajectorySequence;
import lib8812.common.robot.IDriveableRobot;


public class RedRightDoublePixelRunner extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
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

    void armRest()
    {
        bot.arm.setPosition(bot.arm.minPos);
        bot.arm.waitForPosition();
    }

    void dropYellow()
    {
        drive.followTrajectory(drive.trajectoryBuilder(drive.getPoseEstimate()).forward(1).build());
        sleep(300);
        bot.pixelManager.releaseAutonTwoFront();
        sleep(300);
        drive.followTrajectory(drive.trajectoryBuilder(drive.getPoseEstimate()).back(1).build());

        sleep(500);

        armRest();

        sleep(500);
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
        TrajectorySequence postPurple = null;
        TrajectorySequence prepPlace = null;
        TrajectorySequence toPark = null;

        switch (pos)
        {
            case RIGHT:
                toTape = drive.trajectorySequenceBuilder(Autonomous.RED_RIGHT_START)
                        .forward(HALF_BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(-33))
                        .build();
                postPurple = drive.trajectorySequenceBuilder(toTape.end())
                        .turn(Math.toRadians(33))
                        .back(HALF_BLOCK_LENGTH_IN/2)
                        .build();
                prepPlace = drive.trajectorySequenceBuilder(TrajectoryLists.FromRedRight.toRedBackdrop[0].end())
                        .strafeRight(3)
                        .build();
                toPark = drive.trajectorySequenceBuilder(toTape.end())
                        .strafeRight(BLOCK_LENGTH_IN-3)
                        .forward(BLOCK_LENGTH_IN)
                        .build();
                break;
            case CENTER:
                toTape = drive.trajectorySequenceBuilder(Autonomous.RED_RIGHT_START)
                        .forward(HALF_BLOCK_LENGTH_IN)
                        .build();
                postPurple = drive.trajectorySequenceBuilder(toTape.end())
                        .back(HALF_BLOCK_LENGTH_IN/2)
                        .build();
                prepPlace = drive.trajectorySequenceBuilder(TrajectoryLists.FromRedRight.toRedBackdrop[0].end())
                        .build();
                toPark = drive.trajectorySequenceBuilder(toTape.end())
                        .strafeRight(BLOCK_LENGTH_IN)
                        .forward(BLOCK_LENGTH_IN)
                        .build();
                break;
            case LEFT:
                toTape = drive.trajectorySequenceBuilder(Autonomous.RED_RIGHT_START)
                        .forward(HALF_BLOCK_LENGTH_IN/2)
                        .turn(Math.toRadians(33))
                        .build();
                postPurple = drive.trajectorySequenceBuilder(toTape.end())
                        .turn(Math.toRadians(-33))
                        .back(HALF_BLOCK_LENGTH_IN/2)
                        .build();
                prepPlace = drive.trajectorySequenceBuilder(TrajectoryLists.FromRedRight.toRedBackdrop[0].end())
                        .strafeLeft(3)
                        .build();
                toPark = drive.trajectorySequenceBuilder(toTape.end())
                        .strafeRight(BLOCK_LENGTH_IN+3)
                        .forward(BLOCK_LENGTH_IN)
                        .build();
                break;
        }

        drive.followTrajectorySequence(toTape);
        dropPurple();
        drive.followTrajectorySequence(postPurple);
        drive.followTrajectorySequence(TrajectoryLists.FromRedRight.toRedBackdrop[0]);
        drive.followTrajectorySequence(prepPlace);
        dropYellow();
        drive.followTrajectorySequence(toPark);
    }
}
