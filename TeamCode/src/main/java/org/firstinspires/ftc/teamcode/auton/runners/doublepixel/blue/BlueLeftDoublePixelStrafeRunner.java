package org.firstinspires.ftc.teamcode.auton.runners.doublepixel.blue;

import static lib8812.common.auton.autopilot.FieldPositions.Autonomous;
import static lib8812.common.auton.autopilot.FieldPositions.BLOCK_LENGTH_IN;
import static lib8812.common.auton.autopilot.FieldPositions.HALF_BLOCK_LENGTH_IN;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.robot.IDriveableRobot;
import lib8812.common.rr.trajectorysequence.TrajectorySequence;


public class BlueLeftDoublePixelStrafeRunner extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() {
        return bot;
    }

    void armDown()
    {
        bot.clawRotate.setPosition(0.74);
        sleep(300);
        bot.arm.setPosition(bot.arm.maxPos-200);
        bot.arm.waitForPosition();
        bot.arm.setPosition(bot.arm.maxPos-150);
        bot.arm.waitForPosition();
    }

    void armUp()
    {
        bot.arm.setPosition(500);
        bot.arm.waitForPosition();
        bot.clawRotate.setPosition(0.73);
    }

    void dropPurple()
    {
        armDown();
        sleep(300);
        bot.pixelManager.releaseAutonOneFront();
        sleep(300);
        armUp();
        sleep(300);
    }

    void armRest()
    {
        bot.arm.setPosition(bot.arm.minPos);
        bot.arm.waitForPosition();
    }

    void dropYellow()
    {
        bot.arm.setPosition(610);
        bot.arm.waitForPosition();
        bot.arm.setPosition(610);
        bot.arm.waitForPosition();
        sleep(300);
        bot.pixelManager.releaseAutonTwoFront();
        sleep(300);

        armRest();
    }

    protected void internalRun(PixelDetectionConstants.PixelPosition pos) {
        sleep(500); // see below comment
        pos = objectDetector.getCurrentFeed(); // see if this changes anything

        drive.setPoseEstimate(Autonomous.BLUE_LEFT_START);

        TrajectorySequence toTape = null;
        TrajectorySequence toPark = null;
        TrajectorySequence toBackdrop = null;

        switch (pos)
        {
            case RIGHT:
                toTape = drive.trajectorySequenceBuilder(Autonomous.BLUE_LEFT_START)
                        .forward((HALF_BLOCK_LENGTH_IN/2)+2)
                        .turn(Math.toRadians(-20))
                        .build();
                toBackdrop = drive.trajectorySequenceBuilder(toTape.end())
                        .turn(Math.toRadians(20))
                        .back((HALF_BLOCK_LENGTH_IN/2)-4)
                        .turn(Math.toRadians(90))
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(-90))
                        .forward(HALF_BLOCK_LENGTH_IN+7)
                        .turn(Math.toRadians(90))
                        .waitSeconds(0.5)
                        .forward(7)
                        .build();
                toPark = drive.trajectorySequenceBuilder(toBackdrop.end())
//                        .back()
                        .turn(Math.toRadians(90))
                        .waitSeconds(0.5)
                        .forward(HALF_BLOCK_LENGTH_IN+7+1.5)
                        .turn(Math.toRadians(-90))
                        .forward(HALF_BLOCK_LENGTH_IN)
                        .build();
                break;
            case CENTER:
                toTape = drive.trajectorySequenceBuilder(Autonomous.BLUE_LEFT_START)
                        .forward(HALF_BLOCK_LENGTH_IN+3)
                        .turn(Math.toRadians(30))
                        .build();
                toBackdrop = drive.trajectorySequenceBuilder(toTape.end())
                        .turn(Math.toRadians(-30))
                        .back(HALF_BLOCK_LENGTH_IN-3)
                        .strafeLeft(BLOCK_LENGTH_IN)
                        .waitSeconds(1)
                        .forward(BLOCK_LENGTH_IN) // *
                        .turn(Math.toRadians(90))
                        .waitSeconds(0.5)
                        .forward(7)
                        .build();
                toPark = drive.trajectorySequenceBuilder(toBackdrop.end())
//                        .back()
                        .strafeLeft(BLOCK_LENGTH_IN+1.5) // *
                        .forward(HALF_BLOCK_LENGTH_IN)
                        .build();
                break;
            case LEFT:
                toTape = drive.trajectorySequenceBuilder(Autonomous.BLUE_LEFT_START)
                        .forward(HALF_BLOCK_LENGTH_IN/2)
                        .turn(Math.toRadians(41))
                        .build();
                toBackdrop = drive.trajectorySequenceBuilder(toTape.end())
                        .turn(Math.toRadians(-41))
                        .back((HALF_BLOCK_LENGTH_IN/2)-6)
                        .turn(Math.toRadians(90))
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(-90))
                        .forward(BLOCK_LENGTH_IN+HALF_BLOCK_LENGTH_IN-3)
                        .turn(Math.toRadians(90))
                        .forward(7)
                        .build();
                toPark = drive.trajectorySequenceBuilder(toBackdrop.end())
//                        .back()
                        .turn(Math.toRadians(90))
                        .waitSeconds(0.5)
                        .forward(BLOCK_LENGTH_IN+HALF_BLOCK_LENGTH_IN+1.5)
                        .turn(Math.toRadians(-90))
                        .forward(HALF_BLOCK_LENGTH_IN)
                        .build();
                break;
        }

        drive.followTrajectorySequence(toTape);
        dropPurple();
        drive.followTrajectorySequence(toBackdrop);
        dropYellow();
        drive.followTrajectorySequence(toPark);

    }
}
