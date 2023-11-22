package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.teleop.IDriveableRobot;
import lib8812.common.teleop.ITeleopRunner;
import lib8812.common.teleop.TeleOpUtils;

/* RAPTOR TEST RUNNER

-- THE FOLLOWING CONTROLS ARE IMPLEMENTED IN THIS OPMODE --
-- ALL OF THE CONTROLS IMPLEMENTED IN THIS OPMODE ARE EXPERIMENTAL AND NEED EMPIRICAL TESTING --

Gamepad 1

    Right stick - right wheels (with fine tuning, strafe enabled)
    Left stick - left wheels (with fine tuning, strafe enabled)

    Right Trigger - linear actuator up
    Left Trigger - linear actuator down

    Dpad Down - Prep/Ready Plane
    Dpad Up - Shoot Plane

    X Button - Changes verbosity level of info shown in telemetry

Gamepad 2

    Right Bumper - Open right claw
    Right Trigger - Close right claw
    Left Bumper - Open left claw
    Left Trigger - Close left claw

    Right Stick (Y) - Rotate arm forwards/backwards

    Left Stick (Y) - Rotate claw up/down

    X Button - Initializes endgame final sequence
    |   X Button - Used to confirm endgame final sequence
    |   B Button - Cancel sequence

    Right Stick (X) - When pushed in either direction, readies arm/claw in backdrop placing position
    Left Stick Button - Readies arm/claw in pickup position
    Right Stick Button - Rests arm/claw inside of robot

 */

public class RaptorTestRunner extends ITeleopRunner {
    RaptorRobot bot = new RaptorRobot();
    boolean showExtraInfo = false;
    boolean ACTUATOR_LOCKED = false;

    protected IDriveableRobot getBot() { return bot; }

    public double[] testWheelsAndReturnRealInputPower() {
        double correctedRightY = TeleOpUtils.fineTuneInput(gamepad1.right_stick_y, TeleOpUtils.DEFAULT_FINE_TUNE_THRESH);
        double correctedRightX = TeleOpUtils.fineTuneInput(gamepad1.right_stick_x, TeleOpUtils.DEFAULT_FINE_TUNE_THRESH);
        double correctedLeftY = TeleOpUtils.fineTuneInput(gamepad1.left_stick_y, TeleOpUtils.DEFAULT_FINE_TUNE_THRESH);
        double correctedLeftX = TeleOpUtils.fineTuneInput(gamepad1.left_stick_x, TeleOpUtils.DEFAULT_FINE_TUNE_THRESH);

        double correctedRightFront = -correctedRightY-correctedRightX;
        double correctedLeftFront = correctedLeftY-correctedLeftX;
        double correctedRightBack = -correctedRightY+correctedRightX;
        double correctedLeftBack = correctedLeftY+correctedLeftX;

        double realRightFront = -gamepad1.right_stick_y-gamepad1.right_stick_x;
        double realLeftFront = gamepad1.left_stick_y-gamepad1.left_stick_x;
        double realRightBack = -gamepad1.right_stick_y+gamepad1.right_stick_x;
        double realLeftBack = gamepad1.left_stick_y+gamepad1.left_stick_x;

        bot.rightFront.setPower(correctedRightFront);
        bot.leftFront.setPower(correctedLeftFront);
        bot.rightBack.setPower(correctedRightBack);
        bot.leftBack.setPower(correctedLeftBack);

        return new double[] { realLeftFront, realLeftBack, realRightFront, realRightBack };
    }

    public void testActuator() {
        bot.actuator.setPower(gamepad1.right_trigger-gamepad1.left_trigger);
    }

    public void testClaw() {
        gamepad2.map("right_bumper").to(
                () -> bot.clawOne.setLabeledPosition("OPEN")
        ).and("left_bumper").to(
                () -> bot.clawTwo.setLabeledPosition("OPEN")
        ).and("right_trigger").to(
                () -> bot.clawOne.setLabeledPosition("CLOSED")
        ).and("left_trigger").to(
                () -> bot.clawTwo.setLabeledPosition("CLOSED")
        );
    }

    public void testClawRotate() {
        bot.clawRotate.setPosition(
                Math.max(
                        Math.min(
                            bot.clawRotate.getPosition()+
                               gamepad1.left_stick_y/1000
                            , 1
                    ), 0
                )
        );
    }

    public void testPlaneShooter() {
        gamepad1.map("dpad_up").to(
                () -> bot.planeShooter.setLabeledPosition("SHOT")
        ).and("dpad_down").to(
                () -> bot.planeShooter.setLabeledPosition("READY")
        );
    }

    public void testArm() {
        bot.arm.setPosition(
                bot.arm.getPosition()+
                (int) (gamepad1.right_stick_y*5)
        );
    }

    public void endgameSequence() {
        gamepad2.map("x").to(() -> {
            telemetry.addLine("You have initialized the final endgame sequence. Make sure that you are in the correct launching spot. Press X again to confirm or B to cancel. You may not use any other controls until the sequence is confirmed or canceled.");
            telemetry.update();

            while (gamepad2.x); // wait for user to release x first

            while (!(gamepad2.x || gamepad2.b));

            if (gamepad2.b) return;

            if (ACTUATOR_LOCKED) {
                telemetry.addLine("Could not run sequence; the actuator is currently being held by another sequence.");
                telemetry.update();

                return;
            }

            ACTUATOR_LOCKED = true; // make sure no one else sets power to the actuator since it will be running async (see below setTimeout call)

            bot.actuator.setPower(1);

            // shoot plane twice for good measure
            bot.planeShooter.setLabeledPosition("READY");
            bot.planeShooter.setLabeledPosition("SHOT");

            bot.planeShooter.setLabeledPosition("READY");
            bot.planeShooter.setLabeledPosition("SHOT");

            setTimeout(() -> {
                bot.actuator.setPower(0);
                ACTUATOR_LOCKED = false;
            }, 5000);
        });
    }

    public void armSequences() {
        boolean commandArmUp = (gamepad2.right_stick_x > 0.5) || (gamepad2.right_stick_x < -0.5);

        gamepad2.map("left_stick_button").to(() -> { // pickup position macro
            bot.arm.setPosition(bot.arm.maxPos);
            bot.clawRotate.setPosition(0.7);
        }).and("right_stick_button").to(() -> { // resting position macro
            bot.clawRotate.setPosition(0.3);
            bot.arm.setPosition(bot.arm.minPos);
        }).and(commandArmUp).to(() -> { // backdrop place position macro
            bot.arm.setPosition(bot.arm.maxPos*3/4); // 3/4 of the full span
            bot.clawRotate.setPosition(0);
        });
    }

    protected void internalRun() {
        int counter = 0;

        while (opModeIsActive()) {
            double[] realWheelInputPower = testWheelsAndReturnRealInputPower();

            if (!ACTUATOR_LOCKED) testActuator();
            testClaw();
            testPlaneShooter();
            testArm();
            testClawRotate();
            endgameSequence();
            armSequences();

            if (gamepad1.x) showExtraInfo = !showExtraInfo; // telemetry verbosity

            if (showExtraInfo) {
                telemetry.addData(
                        "Wheels (input)",
                        "leftFront (%.2f) leftBack (%.2f) rightFront (%.2f) rightBack (%.2f)",
                        realWheelInputPower[0],
                        realWheelInputPower[1],
                        realWheelInputPower[2],
                        realWheelInputPower[3]
                );
                telemetry.addData(
                        "Wheels (corrected)",
                        "leftFront (%.2f) [tuned by %.2f] leftBack (%.2f) [tuned by %.2f] rightFront (%.2f) [tuned by %.2f] rightBack (%.2f) [tuned by %.2f]",
                        bot.leftFront.getPower(), realWheelInputPower[0] - bot.leftFront.getPower(),
                        bot.leftBack.getPower(), realWheelInputPower[1] - bot.leftBack.getPower(),
                        bot.rightFront.getPower(), realWheelInputPower[2] - bot.rightFront.getPower(),
                        bot.rightBack.getPower(), realWheelInputPower[3] - bot.rightBack.getPower()
                );
            }

            telemetry.addData("Plane Launcher", bot.planeShooter.getPositionLabel());
            telemetry.addData("Claw", "one (%s) two (%s)", bot.clawOne.getPositionLabel(), bot.clawTwo.getPositionLabel());
            telemetry.addData("Actuator", "power (%.2f)%s", bot.actuator.getPower(), ACTUATOR_LOCKED ? " (locked by a sequence)" : "");
            telemetry.addData("Claw Rotate Servo", "pos (%.2f)", bot.clawRotate.getPosition());
            telemetry.addData("Arm", "pos (%.2f)", bot.arm.getPosition());
            telemetry.addData("Verbose", showExtraInfo);

            if (showExtraInfo) {
                for (String key : gamepad1.commonKeyList) {
                    telemetry.addData("Gamepad 1", "%s (%.2f)", key, gamepad1.getValue(key));
                }

                for (String key : gamepad2.commonKeyList) {
                    telemetry.addData("Gamepad 2", "%s (%.2f)", key, gamepad2.getValue(key));
                }
            }

            telemetry.update();

            counter++;
        }
    }
}