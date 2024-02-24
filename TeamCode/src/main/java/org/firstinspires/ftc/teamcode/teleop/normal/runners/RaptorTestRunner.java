package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import java.util.concurrent.TimeUnit;

import lib8812.common.robot.IDriveableRobot;
import lib8812.common.robot.WheelPowers;
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

    Dpad Down [future/unstable] - Runs a pickup sequence which uses the spinning intake to feed pixels into both claws

    -- THE FOLLOWING MACROS ARE EXTREMELY VOLATILE
        (they utilize more controls of the already-in-use joysticks,
        creating a greater chance for mistakes) --

    Right Stick (X) - When pushed (>0.7) in either direction, readies arm/claw in backdrop placing position
    Left Stick (X) -> Right - When pushed (>0.7), closes both claws
    Left Stick (X) -> Left - When pushed (>0.7), opens both claws

    Left Stick Button - Readies arm/claw in pickup position
    Right Stick Button - Rests arm/claw inside of robot

 */

public class RaptorTestRunner extends ITeleopRunner {
    final double MACRO_COMMAND_SAFE_JOYSTICK_THRESH = 0.7;
    final WheelPowers wheelWeights = new WheelPowers(1, 1, 1, 1); // new WheelPowers(0.67, 0.67, 0.65, 1);

    RaptorRobot bot = new RaptorRobot();
    boolean showExtraInfo = false;
    boolean ACTUATOR_LOCKED = false;
    boolean DRIVE_MACRO_LOCKED = false;
    boolean ARM_DOWN_MACRO_LOCKED = false;
    boolean ARM_REST_MACRO_LOCKED = false;

    protected IDriveableRobot getBot() { return bot; }

    public void testWheels() {
        double correctedRightY = TeleOpUtils.fineTuneInput(gamepad1.inner.right_stick_y, TeleOpUtils.DEFAULT_FINE_TUNE_THRESH);
        double correctedRightX = TeleOpUtils.fineTuneInput(gamepad1.inner.right_stick_x, TeleOpUtils.DEFAULT_FINE_TUNE_THRESH);
        double correctedLeftY = TeleOpUtils.fineTuneInput(gamepad1.inner.left_stick_y, TeleOpUtils.DEFAULT_FINE_TUNE_THRESH);
        double correctedLeftX = TeleOpUtils.fineTuneInput(gamepad1.inner.left_stick_x, TeleOpUtils.DEFAULT_FINE_TUNE_THRESH);

        double correctedRightFront = (-correctedRightY-correctedRightX)*wheelWeights.rightFront;
        double correctedLeftFront = (correctedLeftY-correctedLeftX)*wheelWeights.leftFront;
        double correctedRightBack = (-correctedRightY+correctedRightX)*wheelWeights.rightBack;
        double correctedLeftBack = (correctedLeftY+correctedLeftX)*wheelWeights.leftBack;

        bot.rightFront.setPower(correctedRightFront);
        bot.leftFront.setPower(correctedLeftFront);
        bot.rightBack.setPower(correctedRightBack);
        bot.leftBack.setPower(correctedLeftBack);
    }

    public WheelPowers getRealWheelInputPowers() {
        return new WheelPowers(
                -gamepad1.inner.right_stick_y-gamepad1.inner.right_stick_x,
                gamepad1.inner.left_stick_y-gamepad1.inner.left_stick_x,
                -gamepad1.inner.right_stick_y+gamepad1.inner.right_stick_x,
                gamepad1.inner.left_stick_y+gamepad1.inner.left_stick_x
        );
    }

    public void testActuator() {
        if (!ACTUATOR_LOCKED) bot.actuator.setPower(gamepad1.inner.right_trigger-gamepad1.inner.left_trigger);
    }

    public void testIntakes() {
        double spinOnePower =
                gamepad2.getValue("right_bumper") > gamepad2.getValue("right_trigger") ?
                gamepad2.getValue("right_bumper") : -gamepad2.getValue("right_trigger");

        double spinTwoPower =
                gamepad2.getValue("left_bumper") > gamepad2.getValue("left_trigger") ?
                gamepad2.getValue("left_bumper") : -gamepad2.getValue("left_trigger");


        bot.spinOne.setPower(spinOnePower);
        bot.spinTwo.setPower(spinTwoPower);
    }

    public void testClawRotate() {
        bot.clawRotate.setPosition(
                Math.max(
                        Math.min(
                                bot.clawRotate.getPosition() +
                                        (gamepad2.inner.left_stick_y / 3000)
                                , bot.CLAW_ROTATE_MAX
                        ), bot.CLAW_ROTATE_MIN
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
                bot.arm.getPosition()+(int) gamepad2.inner.right_stick_y*2
        );

//        bot.arm.setPower(gamepad2.inner.right_stick_y);
    }

    public void endgameSequence() {
        gamepad2.map("x").to(() -> {
            telemetry.addLine("You have initialized the final endgame sequence. Make sure that you are in the correct launching spot. Press X again to confirm or B to cancel. You may not use any other controls until the sequence is confirmed or canceled.");
            telemetry.update();

            while (gamepad2.inner.x); // wait for user to release x first

            while (!(gamepad2.inner.x || gamepad2.inner.b));

            if (gamepad2.inner.b) return;

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

            while (gamepad2.inner.x);
        });
    }

    public void armSequence_restingPosition() {
        setTimeout(() -> {
            if (ARM_REST_MACRO_LOCKED) return;

            ARM_REST_MACRO_LOCKED = true;

            try {
                bot.clawRotate.setPosition(bot.CLAW_ROTATE_OVER_PLANE_LAUNCHER_POS);
                TimeUnit.MILLISECONDS.sleep(500);
                bot.arm.setPosition(bot.arm.minPos+20);
                bot.arm.waitForPosition();
                bot.clawRotate.setPosition(bot.CLAW_ROTATE_REST_OVER_WHEELS);
                bot.arm.setPosition(bot.arm.minPos);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            ARM_REST_MACRO_LOCKED = false;
        }, 0);
    }

    public void armSequence_backdropPlacePosition() {
        bot.clawRotate.setPosition(bot.STANDARD_DROP_CLAW_ROTATE);
        bot.arm.setPosition(bot.STANDARD_DROP_ARM_TICKS);
    }

    public void armSequence_backdropPrecisionPlacePosition() {
        bot.clawRotate.setPosition(bot.PRECISION_DROP_CLAW_ROTATE);
        bot.arm.setPosition(bot.PRECISION_DROP_ARM_TICKS);
    }

    public void armSequences() {
        boolean commandArmUpStandard = (gamepad2.inner.right_stick_x > MACRO_COMMAND_SAFE_JOYSTICK_THRESH) || (gamepad2.inner.right_stick_x < -MACRO_COMMAND_SAFE_JOYSTICK_THRESH);
        boolean commandArmUpPrecision = gamepad2.inner.a;

        gamepad2.map("left_stick_button").to(() -> setTimeout(() -> {
                if (ARM_DOWN_MACRO_LOCKED) return;

                ARM_DOWN_MACRO_LOCKED = true;

                try {
                    int currPos = bot.arm.getPosition();
                    if (currPos < 50) {
                        bot.arm.setPosition(20);
                        bot.arm.waitForPosition();
                    }

                    bot.clawRotate.setPosition(bot.CLAW_ROTATE_OVER_PLANE_LAUNCHER_POS);

                    TimeUnit.MILLISECONDS.sleep(500);
                    bot.arm.setPosition(bot.arm.maxPos - 400);
                    bot.arm.waitForPosition();

                    bot.clawRotate.setPosition(bot.CLAW_ROTATE_OPTIMAL_PICKUP);
                    TimeUnit.MILLISECONDS.sleep(1000);

                    bot.arm.setPosition(bot.arm.maxPos-1);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }

                ARM_DOWN_MACRO_LOCKED = false;
            }, 0)
        ).and("right_stick_button").to(
                () -> setTimeout(this::armSequence_restingPosition, 0)
        ).and(commandArmUpStandard).to(
                () -> setTimeout(this::armSequence_backdropPlacePosition, 0)
        ).and(commandArmUpPrecision).to(
                () -> setTimeout(this::armSequence_backdropPrecisionPlacePosition, 0)
        );
    }

    public void clawRotateSequences() {
        boolean commandRotateDown = gamepad2.inner.left_stick_x < -MACRO_COMMAND_SAFE_JOYSTICK_THRESH || gamepad2.inner.left_stick_x > MACRO_COMMAND_SAFE_JOYSTICK_THRESH;

        gamepad2.map(commandRotateDown).to(
                () -> bot.clawRotate.setPosition(bot.CLAW_ROTATE_GUARANTEED_PICKUP)
        );

    }

    public void FUTURE_pickupSequence() {
//        gamepad2.map("dpad_down").to(() -> setTimeout(() -> {
//            armSequence_restingPosition();
//            clawSequence_openBoth();
//
//            sleep(300);
//
//            clawSequence_closeBoth();
//
//            armSequence_backdropPlacePosition(); // may need to not use backdrop place position and instead place arm in a shorter position in order to move under the rigging
//        }, 0));
    }

    public void moveExtendRail()
    {
        gamepad2.map("dpad_down").to(
                () -> bot.extendRail.setPosition(
                        Math.min(bot.extendRail.getPosition()+0.0005, bot.MAX_EXTEND)
                )

        ).and("dpad_up").to(
                () -> bot.extendRail.setPosition(
                        Math.max(bot.extendRail.getPosition()-0.0005, bot.MIN_EXTEND)
                )
        );
    }

    protected void internalRun() {
        int counter = 0;

        while (opModeIsActive()) {
            testWheels();
            testActuator();
            testIntakes();
            testPlaneShooter();
            testArm();
            testClawRotate();
            endgameSequence();
            armSequences();
            moveExtendRail();
            clawRotateSequences();
            FUTURE_pickupSequence();

            WheelPowers realWheelInputPowers = getRealWheelInputPowers();

            if (gamepad1.inner.x) showExtraInfo = !showExtraInfo; // telemetry verbosity

            if (showExtraInfo) {
                telemetry.addData(
                        "Wheels (input)",
                        "leftFront (%.2f) leftBack (%.2f) rightFront (%.2f) rightBack (%.2f)",
                        realWheelInputPowers.leftFront,
                        realWheelInputPowers.leftBack,
                        realWheelInputPowers.rightFront,
                        realWheelInputPowers.rightBack
                );
                telemetry.addData(
                        "Wheels (corrected)",
                        "leftFront (%.2f) [tuned by %.2f] leftBack (%.2f) [tuned by %.2f] rightFront (%.2f) [tuned by %.2f] rightBack (%.2f) [tuned by %.2f]",
                        bot.leftFront.getPower(), realWheelInputPowers.leftFront - bot.leftFront.getPower(),
                        bot.leftBack.getPower(), realWheelInputPowers.leftBack - bot.leftBack.getPower(),
                        bot.rightFront.getPower(), realWheelInputPowers.rightFront - bot.rightFront.getPower(),
                        bot.rightBack.getPower(), realWheelInputPowers.rightBack - bot.rightBack.getPower()
                );
            }

            telemetry.addData("Plane Launcher", bot.planeShooter.getPositionLabel());
            telemetry.addData("Extend Rail", "position (%.2f)", bot.extendRail.getPosition());
            telemetry.addData("Intakes", "[power] one (%.2f) two (%.2f)", bot.spinOne.getPower(), bot.spinTwo.getPower());
            telemetry.addData("Actuator", "power (%.2f)%s", bot.actuator.getPower(), ACTUATOR_LOCKED ? " (locked by a sequence)" : "");
            telemetry.addData("Claw Rotate Servo", "pos (%.4f)", bot.clawRotate.getPosition());
            telemetry.addData("Arm", "pos (%d)", bot.arm.getPosition());
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