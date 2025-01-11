
package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.actions.InitAndPredicateAction;
import lib8812.common.actions.MotorSetPositionAction;
import lib8812.common.robot.IMecanumRobot;
import lib8812.common.robot.WheelPowers;
import lib8812.common.telemetrymap.FieldConstants;
import lib8812.common.teleop.ITeleOpRunner;
import lib8812.common.teleop.KeybindPattern;
import lib8812.common.teleop.TeleOpUtils;

public class RaptorMainRunner extends ITeleOpRunner {
    final RaptorRobot bot = new RaptorRobot();
    private final WheelPowers wheelWeights = new WheelPowers(0.92, 0.92, 0.92, 0.92);
    boolean showExtraInfo = false;

    boolean LOCK_INTAKES = false;
    boolean LOCK_ARM = false;
    boolean LOCK_LIFT = false;
    boolean LOCK_WHEELS = false;
    boolean CHANNEL_POWER = false;

    protected IMecanumRobot getBot() { return bot; }

    void moveWheels() {
        double correctedRightY = TeleOpUtils.fineTuneInput(gamepad1.inner.right_stick_y);
        double correctedRightX = TeleOpUtils.fineTuneInput(gamepad1.inner.right_stick_x);
        double correctedLeftY = TeleOpUtils.fineTuneInput(gamepad1.inner.left_stick_y);
        double correctedLeftX = TeleOpUtils.fineTuneInput(gamepad1.inner.left_stick_x);

        WheelPowers correctedWheelPowers = new WheelPowers(
                (-correctedLeftY+correctedLeftX),
                (-correctedLeftY-correctedLeftX),
                (-correctedRightY-correctedRightX),
                (-correctedRightY+correctedRightX)
        );

        correctedWheelPowers.applyTo(bot, wheelWeights);
    }

    WheelPowers getRealWheelInputPowers() {
        return new WheelPowers(
                -gamepad1.inner.left_stick_x+gamepad1.inner.left_stick_x,
                -gamepad1.inner.left_stick_y-gamepad1.inner.left_stick_x,
                -gamepad1.inner.right_stick_y-gamepad1.inner.right_stick_x,
                -gamepad1.inner.right_stick_y+gamepad1.inner.right_stick_x
        );
    }

    void moveSpinningIntake() {
        double inputPwr = Math.signum(gamepad2.inner.left_trigger-gamepad2.inner.right_trigger);

        bot.intakeSmall.setPower(inputPwr); // we don't multiply by the ratio since the servo driver only accepts 1 a power value

        bot.intakeLarge.setPower(-inputPwr*bot.INTAKE_SMALL_TO_LARGE_RADIUS_RATIO);
    }

    void moveLift() {
        bot.extensionLift.setPosition(
                bot.extensionLift.getTargetPosition()-(int) (gamepad2.inner.right_stick_y*50)
        );
    }

    void moveArm() {
        bot.arm.setPosition(
                bot.arm.getTargetPosition()+ (int) (gamepad2.inner.left_stick_y*50)
        );
    }


    void moveClawRotate() {
        double change = 0;

        if (gamepad2.inner.dpad_left) change = -0.01;
        else if (gamepad2.inner.dpad_right) change = 0.01;

        bot.clawRotate.setPosition(
                Math.min(
                        Math.max(
                                bot.clawRotate.getPosition()+change, bot.CLAW_ROTATE_MIN_POS
                        ), bot.CLAW_ROTATE_MAX_POS
                )
        );
    }

    void limitLiftExtension() {
        double alphaDeg = bot.ARM_MAX_ROTATION_DEG*bot.arm.getPosition()/bot.arm.maxPos;

        if (alphaDeg < 90) {
            bot.extensionLift.maxPos = Math.min(
                    (int) Math.floor(
                            ((11/Math.cos(Math.toRadians(alphaDeg)))-bot.ARM_APPROX_LEN_IN)*bot.LIFT_TICKS_PER_INCHES
                    ),
                    bot.LIFT_MAX_TICKS
            );

            return;
        }

        double theta = Math.toRadians(180-alphaDeg);

        bot.extensionLift.maxPos = Math.min(
                (int) Math.floor(
                        ((19.5/Math.abs(Math.cos(theta)))-bot.ARM_APPROX_LEN_IN)*bot.LIFT_TICKS_PER_INCHES
                ), bot.LIFT_MAX_TICKS
        );

        bot.extensionLift.setPosition(bot.extensionLift.getInternalTargetPosition());
    }

    void applyBrakes(DcMotor wheel) {
        wheel.setPower(0);
        wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    void releaseBrakes(DcMotor wheel) {
        wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    void applyBrakesBasedOnInput(float input, DcMotor motor) {
        if (input > 0.3) applyBrakes(motor);
        else releaseBrakes(motor);
    }

    /* MACROS */


    // locking utils
    Action unlockLift() { return new InstantAction(() -> LOCK_LIFT = false); }
    Action unlockArm() { return new InstantAction(() -> LOCK_ARM = false); }
    Action unlockLiftAndArm() { // we must use a single separate InstantAction since this won't consume multiple loop cycles
        return new InstantAction(() -> {
            LOCK_ARM = false;
            LOCK_LIFT = false;
        });
    }

    Action armOut() {
        return new MotorSetPositionAction(bot.arm, bot.arm.maxPos);
    }

    Action armIn() {
        return new SequentialAction(
                new MotorSetPositionAction(bot.arm, bot.arm.minPos),
                new InstantAction(() -> {
                    if (bot.extensionLift.maxPos <= 0) { // recalibrate lift if it has no possible extension
                        bot.extensionLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        bot.extensionLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    }
                }));
    }

    Action liftOut() {
        return new MotorSetPositionAction(bot.extensionLift, bot.extensionLift.maxPos);
    }

    Action liftIn() {
        return new MotorSetPositionAction(bot.extensionLift, bot.extensionLift.minPos);
    }

    void macroLiftFullXXXToggle() {
        if (LOCK_LIFT || CHANNEL_POWER) return;

        LOCK_LIFT = true;

        Action moveLift;

        if (bot.extensionLift.getPosition() < 100) {
            moveLift = liftOut();
        } else {
            moveLift = liftIn();
        }

        actions.schedule(moveLift, unlockLift());
    }

    void macroArmXXXToggle() {
        if (LOCK_ARM || CHANNEL_POWER) return;

        LOCK_ARM = true;

        Action moveArm;

        if (bot.arm.getPosition() < (bot.arm.maxPos-bot.arm.minPos)/2) {
            moveArm = armOut();
        }
        else {
            moveArm = armIn();
        }

        actions.schedule(moveArm, unlockArm());
    }

    void macroFrog() {
        if (LOCK_INTAKES || LOCK_ARM || LOCK_LIFT || CHANNEL_POWER) return;

        // lift locking is not needed since both lift and arm now use targetPosition to calculate new pos
        LOCK_INTAKES = LOCK_ARM = LOCK_LIFT = true;

        double alphaDeg = bot.ARM_MAX_ROTATION_DEG*bot.arm.getPosition()/bot.arm.maxPos;
        double thetaDeg = 270-alphaDeg;
        double theta = Math.toRadians(thetaDeg);

        double liftToGroundExtIn = (bot.ARM_JOINT_MOUNT_HEIGHT_IN/Math.cos(theta))-bot.ARM_APPROX_LEN_IN;

        int liftToGroundExtTicksReal = (int) Math.floor(liftToGroundExtIn*bot.LIFT_TICKS_PER_INCHES);

        int liftToGroundExtTicksEnsure = liftToGroundExtTicksReal+25; // ensure we hit the ground (but don't waste too much time) by adding a small amount of ticks

        actions.schedule(
                new InstantAction(() -> {
                    bot.intakeLarge.setPower(bot.INTAKE_LARGE_IN_DIRECTION);
                    bot.intakeSmall.setPower(bot.INTAKE_SMALL_IN_DIRECTION);

//                    bot.lilRaptor.setPosition(bot.LIL_RAPTOR_OUT_POS);
                }),
                new MotorSetPositionAction(bot.extensionLift, liftToGroundExtTicksEnsure), // be safe to not violate the extension limit
				telemetryPacket -> bot.extensionLift.isBusy(),
//                new InstantAction(() -> bot.lilRaptor.setPosition(bot.LIL_RAPTOR_REST_POS)),
                new MotorSetPositionAction(bot.extensionLift, bot.extensionLift.minPos),
                new InstantAction(() -> {
                    bot.intakeLarge.setPower(0);
                    bot.intakeSmall.setPower(0);

                    LOCK_INTAKES = LOCK_ARM = LOCK_LIFT = false;
                })
        );
    }

    void macroPrepareForReverseHighDrop() {
        if (LOCK_ARM || LOCK_LIFT || CHANNEL_POWER) return;

        LOCK_ARM = true;
        LOCK_LIFT = true;

        actions.schedule(
                new InitAndPredicateAction(
                        () -> bot.arm.setPosition(bot.REVERSE_DROP_MACRO_ARM_POS),
                        () -> bot.extensionLift.maxPos >= bot.REVERSE_DROP_MACRO_LIFT_POS
                ),
                new MotorSetPositionAction(bot.extensionLift, bot.REVERSE_DROP_MACRO_LIFT_POS),
                new MotorSetPositionAction(bot.arm, bot.REVERSE_DROP_MACRO_ARM_POS),
                unlockLiftAndArm()
        );
    }

    Action hangSpecimenBackHighChamberInternal() {
        return new SequentialAction(
                new InstantAction(() -> {
                    /*  clutch the specimen */
                    bot.intakeSmall.setPower(bot.INTAKE_SMALL_IN_DIRECTION);
                    bot.intakeLarge.setPower(bot.INTAKE_LARGE_IN_DIRECTION);
                }),
                /* hook the specimen onto the high chamber and wait for at least 0.5 sec */
                new MotorSetPositionAction(bot.extensionLift, 500),
                telemetryPacket -> bot.extensionLift.isBusy(),
                new MotorSetPositionAction(bot.arm, bot.BACKWARDS_HIGH_CHAMBER_ARM_POS-150),
                telemetryPacket -> bot.arm.isBusy(),
                new MotorSetPositionAction(bot.extensionLift, bot.extensionLift.minPos),
                telemetryPacket -> bot.extensionLift.isBusy(),
                new InstantAction(() -> {
                    /* once the specimen is secured to the high chamber, forcefully release it and raise the arm */
                    bot.intakeSmall.setPower(bot.INTAKE_SMALL_OUT_DIRECTION);
                    bot.intakeLarge.setPower(bot.INTAKE_LARGE_OUT_DIRECTION);
                }),
                new MotorSetPositionAction(bot.arm, bot.BACKWARDS_HIGH_CHAMBER_ARM_POS),
                telemetryPacket -> bot.arm.isBusy(),
                new InstantAction(() -> {
                    bot.intakeSmall.setPower(0);
                    bot.intakeLarge.setPower(0);
                })
        );
    }

    void macroHangSpecimenBackHighChamber() {
        if (LOCK_ARM || LOCK_INTAKES || LOCK_LIFT || CHANNEL_POWER) return;

        LOCK_ARM = LOCK_INTAKES = LOCK_LIFT = true;

        actions.schedule(
                hangSpecimenBackHighChamberInternal(),
                new InstantAction(
                        () -> {
                            LOCK_ARM = LOCK_INTAKES = LOCK_LIFT = false;
                        }
                )
        );
    }

    void macroPrepareForForwardHighDrop() {
        if (LOCK_ARM || LOCK_LIFT || CHANNEL_POWER) return;

        LOCK_ARM = true;
        LOCK_LIFT = true;

        actions.schedule(
                new SequentialAction(
                        new InitAndPredicateAction(
                                () -> bot.arm.setPosition(bot.FORWARDS_HIGH_BASKET_ARM_POS),
                                () -> bot.extensionLift.maxPos >= bot.FORWARDS_HIGH_BASKET_LIFT_POS)
                        ),
                new MotorSetPositionAction(bot.extensionLift, bot.FORWARDS_HIGH_BASKET_LIFT_POS),
                new MotorSetPositionAction(bot.arm, bot.FORWARDS_HIGH_BASKET_ARM_POS),
                unlockLiftAndArm()
        );
    }

    void macroAutoHangSpecimenFromOZ() {
        if (LOCK_WHEELS || LOCK_INTAKES || LOCK_ARM || LOCK_LIFT || CHANNEL_POWER) return;

        LOCK_WHEELS = LOCK_INTAKES = LOCK_ARM = LOCK_LIFT = true;

        final Pose2d initialSpecimenPickupPose = new Pose2d(2* FieldConstants.BLOCK_LENGTH_IN, -(2*FieldConstants.BLOCK_LENGTH_IN-5), 3 * Math.PI / 2);
        final Pose2d posForSpecimenDrop = new Pose2d(0.2*FieldConstants.BLOCK_LENGTH_IN, -(1.5*FieldConstants.BLOCK_LENGTH_IN), 3 * Math.PI / 2);

        bot.setRRDrivePose(initialSpecimenPickupPose);

        Action prepForHang = new SequentialAction(
                new MotorSetPositionAction(bot.arm, bot.BACKWARDS_HIGH_CHAMBER_ARM_POS),
                bot.drive.actionBuilder(initialSpecimenPickupPose)
                        .strafeToSplineHeading(posForSpecimenDrop.position, posForSpecimenDrop.heading)
                        .build()
        );

        Action returnToOZ = new ParallelAction( // keep the arm at bot.BACKWARDS_HIGH_CHAMBER_ARM_POS so that drivers don't waste too much time bringing the arm back out for a frog
                bot.drive.actionBuilder(posForSpecimenDrop)
                        .strafeToSplineHeading(initialSpecimenPickupPose.position, posForSpecimenDrop.heading)
                        .build()
        );

        Action macro = new SequentialAction(
                prepForHang,
                hangSpecimenBackHighChamberInternal(),
                returnToOZ,
                new InstantAction(() -> LOCK_WHEELS = LOCK_INTAKES = LOCK_ARM = LOCK_LIFT = false)
        );

        actions.schedule(macro);
    }

    /* END MACROS */

    void tryRecoverFromAuton() {
        telemetry.addData("RAPTOR RECOVERY MODE", "enabled");
        telemetry.addData("RAPTOR RECOVERY MODE", "PRESS B TO RECOVER");
        telemetry.update();

        while (!gamepad1.inner.b) {
            moveWheels();
        }

//        bot.arm.reverse();
//        bot.extensionLift.reverse();


//        bot.arm.setPosition(InteropFields.ARM_END_POS);
//        bot.extensionLift.setPosition(InteropFields.LIFT_END_POS);

//        bot.arm.waitForPosition();
//        bot.extensionLift.waitForPosition();
//
//        bot.arm.resetEncoder();
//        bot.extensionLift.resetEncoder();
//
//        bot.arm.reverse();
//        bot.extensionLift.reverse();

        bot.arm.resetEncoder();
        bot.extensionLift.resetEncoder();
    }

    protected void internalRun() {
        KeybindPattern.GamepadBinder x = keybinder.bind("x");

        x.of(gamepad1).to(() -> showExtraInfo = !showExtraInfo);

        // hang bind release
        x.of(gamepad2).to(() -> {
            bot.arm.maxPos = bot.ARM_HANG_MAX_TICKS;

            CHANNEL_POWER = true;

            bot.extensionLift.setPower(0);
            bot.extensionLift.close();

            bot.clawRotate.getController().pwmDisable();
            bot.clawRotate.close();

            bot.intakeSmall.getController().pwmDisable();
            bot.intakeSmall.close();

            bot.intakeLarge.getController().pwmDisable();
            bot.intakeLarge.close();

            telemetry.addData("no telemetry data", "arm power channel mode");
            telemetry.addData("note", "to recover, bring arm back to resting position and restart opmode");
            telemetry.update();
        });
        
        keybinder.bind("left_stick_button").of(gamepad2).to(this::macroArmXXXToggle);
        keybinder.bind("right_stick_button").of(gamepad2).to(this::macroLiftFullXXXToggle);

        keybinder.bind("dpad_up").of(gamepad2).to(this::macroPrepareForForwardHighDrop);
        keybinder.bind("a").of(gamepad2).to(this::macroHangSpecimenBackHighChamber);

        keybinder.bind("right_bumper").of(gamepad2).to(this::macroFrog);
        keybinder.bind("left_bumper").of(gamepad2).to(this::macroPrepareForReverseHighDrop);

        keybinder.bind("left_trigger").of(gamepad1).to((inp) -> {
            applyBrakesBasedOnInput(inp, bot.leftFront);
            applyBrakesBasedOnInput(inp, bot.leftBack);
        });

        keybinder.bind("right_trigger").of(gamepad1).to((inp) -> {
            applyBrakesBasedOnInput(inp, bot.rightFront);
            applyBrakesBasedOnInput(inp, bot.rightBack);
        });

        keybinder.bind("b").of(gamepad1).to(() -> { // driver one any action/macro cancellation
           actions.clear();

           // macros may have locked devices and not unlocked them due to the cancellation, so unlock everything here
           LOCK_LIFT = false;
           LOCK_ARM = false;
           LOCK_INTAKES = false;
           LOCK_WHEELS = false;
        });

        keybinder.bind("a").of(gamepad1).to(this::macroAutoHangSpecimenFromOZ); // on driver one gamepad, automatically hangs specimen and returns to OZ

        tryRecoverFromAuton();

        while (opModeIsActive()) {
            if (!LOCK_WHEELS) moveWheels();

            WheelPowers realWheelInputPowers = getRealWheelInputPowers();

            if (!LOCK_ARM) moveArm();
            limitLiftExtension();
            if (!LOCK_LIFT && !CHANNEL_POWER) moveLift();
            if (!CHANNEL_POWER) moveClawRotate();
            if (!LOCK_INTAKES && !CHANNEL_POWER) moveSpinningIntake();

            keybinder.executeActions();
            actions.execute();

            if (CHANNEL_POWER) continue;

            telemetry.addData(
                    "claw rotate", "pos (%.2f)",
                    bot.clawRotate.getPosition()
            );

            telemetry.addData(
                    "intake (small)", "power (%.2f)%s",
                    bot.intakeSmall.getPower(),
                    (LOCK_INTAKES ? " locked" : "")
            );

            telemetry.addData(
                    "intake (large)", "power (%.2f)%s",
                    bot.intakeLarge.getPower(),
                    (LOCK_INTAKES ? " locked" : "")
            );

            telemetry.addData(
                    "extension lift", "pos (%d), target (%d) power (%.2f) max pos (%d)%s",
                    bot.extensionLift.getPosition(), bot.extensionLift.getTargetPosition(),
                    bot.extensionLift.getPower(), bot.extensionLift.maxPos,
                    (LOCK_LIFT ? " locked" : "")
            );

            telemetry.addData(
                    "arm", "pos (%d), target (%d), power (%.2f) max pos (%d) approx. alpha (%.3f deg) max alpha (%.3f deg)%s",
                    bot.arm.getPosition(), bot.arm.getTargetPosition(),
                    bot.arm.getPower(), bot.arm.maxPos,
                    bot.ARM_MAX_ROTATION_DEG*bot.arm.getPosition()/bot.arm.maxPos, // approx. alpha
                    bot.ARM_MAX_ROTATION_DEG, // max alpha
                    (LOCK_ARM ? " locked" : "")
            );

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

            telemetry.addData("Actions", "length (%d)", actions.count());
            telemetry.addData("Verbosity Level", "%s", showExtraInfo ? "high" : "low");

            if (showExtraInfo) {
                for (String key : gamepad1.commonKeyList) {
                    telemetry.addData("Gamepad 1", "%s (%.2f)", key, gamepad1.getValue(key));
                }

                for (String key : gamepad2.commonKeyList) {
                    telemetry.addData("Gamepad 2", "%s (%.2f)", key, gamepad2.getValue(key));
                }
            }

            telemetry.update();
        }

        bot.deInit();
    }
}