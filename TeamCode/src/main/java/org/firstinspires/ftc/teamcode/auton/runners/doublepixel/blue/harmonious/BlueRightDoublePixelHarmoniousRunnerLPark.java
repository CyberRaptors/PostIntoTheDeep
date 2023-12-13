package org.firstinspires.ftc.teamcode.auton.runners.doublepixel.blue.harmonious;

import static lib8812.common.auton.autopilot.FieldPositions.Autonomous;
import static lib8812.common.auton.autopilot.FieldPositions.BLOCK_LENGTH_IN;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.auton.autopilot.TrajectoryLists;
import lib8812.common.rr.trajectorysequence.TrajectorySequence;
import lib8812.common.robot.IDriveableRobot;


public class BlueRightDoublePixelHarmoniousRunnerLPark extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
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

        TrajectorySequence program = null;

        switch (pos)
        {
            case RIGHT:
                program = drive.trajectorySequenceBuilder(Autonomous.BLUE_RIGHT_START)
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(45))
                        .addTemporalMarker(this::dropPurple)
                        .turn(Math.toRadians(-45))
                        .back(BLOCK_LENGTH_IN)
                        .addTemporalMarker(() -> drive.followTrajectorySequence(TrajectoryLists.FromBlueRight.Harmonious.toBlueBackdrop[0]))
                        .strafeRight(5) // drop in right section
                        .addTemporalMarker(this::dropYellow)
                        .forward(5) // make sure we are in backstage, remove if not necessary
                        .build();
                break;
            case CENTER:
                program = drive.trajectorySequenceBuilder(Autonomous.BLUE_RIGHT_START)
                        .forward(BLOCK_LENGTH_IN)
                        .addTemporalMarker(this::dropPurple)
                        .back(BLOCK_LENGTH_IN)
                        .addTemporalMarker(() -> drive.followTrajectorySequence(TrajectoryLists.FromBlueRight.Harmonious.toBlueBackdrop[0]))
                        .addTemporalMarker(this::dropYellow)
                        .forward(5) // make sure we are in backstage, remove if not necessary
                        .build();
                break;
            case LEFT:
                program = drive.trajectorySequenceBuilder(Autonomous.BLUE_RIGHT_START)
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(-45))
                        .addTemporalMarker(this::dropPurple)
                        .turn(Math.toRadians(45))
                        .back(BLOCK_LENGTH_IN)
                        .addTemporalMarker(() -> drive.followTrajectorySequence(TrajectoryLists.FromBlueRight.Harmonious.toBlueBackdrop[0]))
                        .strafeLeft(5) // drop in right section
                        .addTemporalMarker(this::dropYellow)
                        .forward(5) // make sure we are in backstage, remove if not necessary
                        .build();
                break;
        }



        drive.followTrajectorySequence(program);
    }
}
