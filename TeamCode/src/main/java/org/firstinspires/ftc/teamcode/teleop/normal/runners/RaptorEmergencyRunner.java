
package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.robot.WheelPowers;
import lib8812.common.teleop.ITeleOpRunner;
import lib8812.common.teleop.TeleOpUtils;

public class RaptorEmergencyRunner extends ITeleOpRunner {
    final ActionableRaptorRobot bot = new ActionableRaptorRobot();
    private final WheelPowers wheelWeights = new WheelPowers(0.92, 0.92, 0.92, 0.92);
    boolean showExtraInfo = false;

    boolean LOCK_INTAKES = false;
    boolean LOCK_ARM = false;
    boolean LOCK_LIFT = false;
    boolean LOCK_WHEELS = false;
    boolean CHANNEL_POWER = false;

    long msAtAuxClose;

    protected IMecanumRobot getBot() { return bot; }

    void moveWheels() {
        // Take the average of both gamepads' power
        double greatestXValue = (gamepad1.inner.right_stick_x+gamepad1.inner.left_stick_x)/1.5;
        double greatestYValue = (gamepad1.inner.right_stick_y+gamepad1.inner.left_stick_y)/1.5;

        // swap y and x here as the robot's position is technically rotated by PI/2 radians
        double yPower = -TeleOpUtils.fineAndFastControl(greatestXValue);
        double xPower = -TeleOpUtils.fineAndFastControl(greatestYValue);

        double turnPower = (gamepad1.inner.left_trigger-gamepad1.inner.right_trigger)*0.8;

        if (Math.signum(gamepad1.inner.right_stick_y) == -Math.signum(gamepad1.inner.left_stick_y) && Math.signum(gamepad1.inner.right_stick_y) != 0) {
            turnPower = (gamepad1.inner.right_stick_y-gamepad1.inner.left_stick_y)*0.5;
        }

        bot.drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        xPower*0.9,
                        yPower*0.9
                ),
                turnPower*0.9
        ));

    }

    WheelPowers getRealWheelInputPowers() {
        return new WheelPowers(
                -gamepad1.inner.left_stick_x+gamepad1.inner.left_stick_x,
                -gamepad1.inner.left_stick_y-gamepad1.inner.left_stick_x,
                -gamepad1.inner.right_stick_y-gamepad1.inner.right_stick_x,
                -gamepad1.inner.right_stick_y+gamepad1.inner.right_stick_x
        );
    }

    protected void internalRun() {
        bot.limelightMgr.init(); // apparently the Limelight should only be started *after* waitForStart()

        keybinder.bind("left_stick_y").of(gamepad2).to(bot.arm::setPower);
        keybinder.bind("right_stick_y").of(gamepad2).to(bot.extensionLift::setPower);

        while (opModeIsActive()) {
            moveWheels();

            WheelPowers realWheelInputPowers = getRealWheelInputPowers();

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
                    "aux system",
                    "claw (%s) rotate (%s)",
                    bot.auxClaw.inner.getPositionLabel(),
                    bot.auxClawRotate.getPositionLabel()
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

            boolean limelightConn = bot.limelightMgr.inner.isConnected();
            boolean limelightRun = bot.limelightMgr.inner.isRunning();


            telemetry.addData("Limelight", "connected (%b) running (%b)", limelightConn, limelightRun);

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