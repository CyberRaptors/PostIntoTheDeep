
package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.robot.WheelPowers;
import lib8812.common.teleop.ITeleOpRunner;
import lib8812.common.teleop.KeybindPattern;
import lib8812.common.teleop.TeleOpUtils;

public class RaptorMainRunner extends ITeleOpRunner {
    final RaptorRobot bot = new RaptorRobot();
    private final WheelPowers wheelWeights = new WheelPowers(1, 1, 1, 1);
    boolean showExtraInfo = false;

    boolean LOCK_INTAKES = false;
    boolean LOCK_ARM = false;
    boolean LOCK_LIFT = false;

    final static Runnable defaultResolver = () -> {};

    Runnable onArmResolved = defaultResolver;
    Runnable onLiftResolved = defaultResolver;

    protected IMecanumRobot getBot() { return bot; }

    void testWheels() {
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
        double inputPwr = gamepad2.inner.right_trigger-gamepad2.inner.left_trigger;

        bot.intakeSmall.setPower(inputPwr); // we don't multiply by the ratio since the servo driver only accepts 1 a power value

        bot.intakeLarge.setPower(-inputPwr);
    }

    void moveLift() {
        bot.extensionLift.setPosition(
                bot.extensionLift.getTargetPosition()-(int) (gamepad2.inner.right_stick_y*50)
        );
    }

    void moveArm() {
        bot.arm.setPosition(
                bot.arm.getPosition()+ (int) (gamepad2.inner.left_stick_y*50)
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

    void moveActuator() {
        bot.actuator.setPower(
                gamepad1.inner.left_trigger-gamepad1.inner.right_trigger
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

    void macroArmOut() {
        bot.arm.setPosition(bot.arm.maxPos);
    }

    void macroArmIn() {
        bot.arm.setPosition(bot.arm.minPos);
    }

    void macroLiftFullExtension() { bot.extensionLift.setPosition(bot.extensionLift.maxPos); }

    void macroLiftFullRetract() { bot.extensionLift.setPosition(bot.extensionLift.minPos); }

    void macroLiftFullXXXToggle() {
        if (LOCK_LIFT) return;

        LOCK_LIFT = true;

        if (bot.extensionLift.getPosition() < 100) {
            macroLiftFullExtension();
        } else {
            macroLiftFullRetract();
        }

        onLiftResolved = () -> LOCK_LIFT = false;
    }

    void macroArmXXXToggle() {
        if (LOCK_ARM) return;

        LOCK_ARM = true;

        if (bot.arm.getPosition() < 500) {
            macroArmOut();
        }
        else {
            macroArmIn();
        }

        onArmResolved = () -> LOCK_ARM = false;
    }

    void macroFrog() {
        if (LOCK_ARM || LOCK_LIFT || LOCK_INTAKES) return;

        LOCK_ARM = true;
        LOCK_LIFT = true;
        LOCK_INTAKES = true;

        double alphaDeg = bot.ARM_MAX_ROTATION_DEG*bot.arm.getPosition()/bot.arm.maxPos;
        double thetaDeg = 270-alphaDeg;
        double theta = Math.toRadians(thetaDeg);

        double liftToGroundExtIn = (bot.ARM_JOINT_MOUNT_HEIGHT_IN/Math.cos(theta))-bot.ARM_APPROX_LEN_IN;

        int liftToGroundExtTicksReal = (int) Math.floor(liftToGroundExtIn*bot.LIFT_TICKS_PER_INCHES);

        int liftToGroundExtTicksEnsure = liftToGroundExtTicksReal+75;

        bot.intakeLarge.setPower(bot.INTAKE_LARGE_IN_DIRECTION);
        bot.intakeSmall.setPower(bot.INTAKE_SMALL_IN_DIRECTION);

        bot.lilRaptor.setPosition(bot.LIL_RAPTOR_OUT_POS);

        bot.extensionLift.setPosition(liftToGroundExtTicksEnsure); // be safe to not violate the extension limit (since lift limiting is not enabled during locking)

        onLiftResolved = () -> {
            bot.lilRaptor.setPosition(bot.LIL_RAPTOR_REST_POS);

            bot.extensionLift.setPosition(bot.extensionLift.minPos+50); // maybe we do 0+50 position to reduce risk of causing a macro deadlock

            onLiftResolved = () -> {
                bot.intakeLarge.setPower(0);
                bot.intakeSmall.setPower(0);

                LOCK_LIFT = false;
                LOCK_INTAKES = false;
                LOCK_ARM = false;
            };
        };
    }

    void macroPrepareForReverseHighDrop() {
        if (LOCK_ARM || LOCK_LIFT) return;

        LOCK_ARM = true;
        LOCK_LIFT = true;

        bot.arm.setPosition(bot.REVERSE_DROP_MACRO_ARM_POS);

        onArmResolved =  () -> {
            LOCK_ARM = false;
            bot.extensionLift.setPosition(bot.REVERSE_DROP_MACRO_LIFT_POS);

            onLiftResolved = () -> LOCK_LIFT = false;
        };
    }

    void macroArmBackForHighChamber() {
        if (LOCK_ARM) return;

        LOCK_ARM = true;

        bot.arm.setPosition(bot.BACKWARDS_HIGH_CHAMBER_ARM_POS);

        onArmResolved = () -> LOCK_ARM = false;
    }

    void macroPrepareForForwardHighDrop() {
        if (LOCK_ARM || LOCK_LIFT) return;

        LOCK_ARM = true;
        LOCK_LIFT = true;

        bot.arm.setPosition(bot.FORWARDS_HIGH_BASKET_ARM_POS);

        onArmResolved =  () -> {
            LOCK_ARM = false;
            bot.extensionLift.setPosition(bot.FORWARDS_HIGH_BASKET_LIFT_POS);

            onLiftResolved = () -> LOCK_LIFT = false;
        };
    }

    // macro utility
    void tryClearResolvers() {
        if (!LOCK_ARM) onArmResolved = defaultResolver;

        if (!LOCK_LIFT) onLiftResolved = defaultResolver;
    }

    /* END MACROS */

    protected void internalRun() {
        KeybindPattern.GamepadBinder x = keybinder.bind("x");

        x.of(gamepad1).to(() -> showExtraInfo = !showExtraInfo);

        // hang bind release
        x.of(gamepad2).to(() -> {
            if (bot.arm.maxPos == bot.ARM_HANG_MAX_TICKS) {
                bot.arm.maxPos = bot.ARM_MAX_TICKS;
            } else bot.arm.maxPos = bot.ARM_HANG_MAX_TICKS;
        });

        keybinder.bind("y").of(gamepad2).to(() -> LOCK_ARM = !LOCK_ARM);

        keybinder.bind("left_stick_button").of(gamepad2).to(this::macroArmXXXToggle);
        keybinder.bind("right_stick_button").of(gamepad2).to(this::macroLiftFullXXXToggle);

        keybinder.bind("dpad_up").of(gamepad2).to(this::macroPrepareForForwardHighDrop);
        keybinder.bind("a").of(gamepad2).to(this::macroArmBackForHighChamber);

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

        while (opModeIsActive()) {
            testWheels();

            WheelPowers realWheelInputPowers = getRealWheelInputPowers();

            tryClearResolvers();

            if (LOCK_ARM && !bot.arm.isBusy()) onArmResolved.run(); // arm-based macro resolver
            if (LOCK_LIFT && !bot.extensionLift.isBusy()) onLiftResolved.run(); // lift-based macro resolver


            if (!LOCK_ARM) moveArm();
            limitLiftExtension();
            if (!LOCK_LIFT) moveLift();
            moveClawRotate();
            moveActuator();
            if (!LOCK_INTAKES) moveSpinningIntake();

            keybinder.executeActions();

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

            telemetry.addData(
                    "actuator", "power (%.2f)",
                    bot.actuator.getPower()
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
    }
}