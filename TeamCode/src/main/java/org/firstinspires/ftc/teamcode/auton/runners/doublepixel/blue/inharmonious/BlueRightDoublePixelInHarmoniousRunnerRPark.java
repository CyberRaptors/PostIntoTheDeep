package org.firstinspires.ftc.teamcode.auton.runners.doublepixel.blue.inharmonious;

import static lib8812.common.auton.autopilot.FieldPositions.Autonomous;
import static lib8812.common.auton.autopilot.FieldPositions.BLOCK_LENGTH_IN;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.auton.autopilot.TrajectoryLists;
import lib8812.common.rr.trajectorysequence.TrajectorySequence;
import lib8812.common.robot.IDriveableRobot;


public class BlueRightDoublePixelInHarmoniousRunnerRPark extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() {
        return bot;
    }


    void dropPurple()
    {
//        bot.testLift1.setPower(0.2); // raise lifts a little
//        bot.testLift2.setPower(0.2);
//        bot.arm.setPower(0.75); // out facing tape on floor
//        bot.claw.setPosition(0); // open
//        bot.arm.setPower(0); // by spinning intake, waiting for yellow to be fed in
//        bot.spinningIntake.setPower(1); // take in yellow pixel;
//        sleep(100);
//        bot.spinningIntake.setPower(0);
//        bot.claw.setPosition(1); // clasp yellow pixel
//        bot.testLift1.setPower(0);
//        bot.testLift2.setPower(0);
    }

    void dropYellow()
    {
//        bot.testLift1.setPower(0.2); // raise lifts a little
//        bot.testLift2.setPower(0.2);
//        bot.arm.setPower(1); // out facing backboard
//        bot.claw.setPosition(0); // open
//        bot.arm.setPower(0); // downwards but not interfering with lift
//        bot.testLift1.setPower(0);
//        bot.testLift2.setPower(0);
    }

    protected void internalRun(PixelDetectionConstants.PixelPosition pos) {
        sleep(1000); // see below comment
        pos = objectDetector.getCurrentFeed(); // see if this changes anything

        drive.setPoseEstimate(Autonomous.BLUE_RIGHT_START);

        TrajectorySequence toTape = null;
        TrajectorySequence prepGoToBackdrop = null;
        TrajectorySequence prepPlace = null;
        TrajectorySequence toPark = null;

        switch (pos) {
            case RIGHT:
                toTape = drive.trajectorySequenceBuilder(Autonomous.BLUE_RIGHT_START)
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(45))
                        .build();
                prepGoToBackdrop = drive.trajectorySequenceBuilder(toTape.end())
                        .turn(Math.toRadians(-45))
                        .back(BLOCK_LENGTH_IN)
                        .build();
                prepPlace = drive.trajectorySequenceBuilder(TrajectoryLists.FromBlueRight.InHarmonious.toBlueBackdrop[0].end())
                        .strafeRight(5) // drop in right section
                        .build();
                toPark = drive.trajectorySequenceBuilder(prepPlace.end())
                        .strafeRight(BLOCK_LENGTH_IN - 5)
                        .forward(BLOCK_LENGTH_IN) // make sure we are in backstage, remove if not necessary
                        .build();
                break;
            case CENTER:
                toTape = drive.trajectorySequenceBuilder(Autonomous.BLUE_RIGHT_START)
                        .forward(BLOCK_LENGTH_IN)
                        .build();
                prepGoToBackdrop = drive.trajectorySequenceBuilder(toTape.end())
                        .back(BLOCK_LENGTH_IN)
                        .build();
                prepPlace = drive.trajectorySequenceBuilder(TrajectoryLists.FromBlueRight.InHarmonious.toBlueBackdrop[0].end())
                        .build();
                toPark = drive.trajectorySequenceBuilder(prepPlace.end())
                        .strafeRight(BLOCK_LENGTH_IN)
                        .forward(BLOCK_LENGTH_IN) // make sure we are in backstage, remove if not necessary
                        .build();
                break;
            case LEFT:
                toTape = drive.trajectorySequenceBuilder(Autonomous.BLUE_RIGHT_START)
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(-45))
                        .build();
                prepGoToBackdrop = drive.trajectorySequenceBuilder(toTape.end())
                        .turn(Math.toRadians(45))
                        .back(BLOCK_LENGTH_IN)
                        .build();
                prepPlace = drive.trajectorySequenceBuilder(TrajectoryLists.FromBlueRight.InHarmonious.toBlueBackdrop[0].end())
                        .strafeLeft(5)
                        .build();
                toPark = drive.trajectorySequenceBuilder(prepPlace.end())
                        .strafeRight(BLOCK_LENGTH_IN + 5)
                        .forward(BLOCK_LENGTH_IN) // make sure we are in backstage, remove if not necessary
                        .build();
                break;
        }

        drive.followTrajectorySequence(toTape);
        dropPurple();
        drive.followTrajectorySequence(prepGoToBackdrop);
        drive.followTrajectorySequence(TrajectoryLists.FromBlueRight.InHarmonious.toBlueBackdrop[0]);
        drive.followTrajectorySequence(prepPlace);
        dropYellow();
        drive.followTrajectorySequence(toPark);
    }
}
