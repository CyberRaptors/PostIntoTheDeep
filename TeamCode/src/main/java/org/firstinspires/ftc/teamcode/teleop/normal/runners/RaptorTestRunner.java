package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.teleop.IDriveableRobot;
import lib8812.common.teleop.ITeleopRunner;

public class RaptorTestRunner extends ITeleopRunner {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() { return bot; };

    private double planeLauncherPower = 0;

    public void testPlaneLauncher() {
        if (gamepad2.dpad_up) {
            planeLauncherPower = 0.7;
        }

        if (gamepad2.dpad_down) {
            planeLauncherPower = 0;
        }

        bot.planeLauncher.setPower(planeLauncherPower);
    }

    public void testLifts() {
        bot.testLift1.setPower(-gamepad2.right_stick_y);
        bot.testLift2.setPower(gamepad2.right_stick_y);
    }

    public void testWheels() {
        bot.rightFront.setPower(-gamepad1.right_stick_y-gamepad1.right_stick_x);
        bot.leftFront.setPower(gamepad1.left_stick_y-gamepad1.left_stick_x);
        bot.rightBack.setPower(-gamepad1.right_stick_y+gamepad1.right_stick_x);
        bot.leftBack.setPower(gamepad1.left_stick_y+gamepad1.left_stick_x);
    }

    public void testHangServos() {
        if (gamepad1.dpad_down) {
            bot.hangServoLeft.setPower(-0.1);
            bot.hangServoRight.setPower(0.1);

            setTimeout(
                () -> {
                   bot.hangServoLeft.setPower(0);
                   bot.hangServoRight.setPower(0);
                }, 1500
            );
        } else if (gamepad1.dpad_up) {
            bot.hangServoLeft.setPower(0.1);
            bot.hangServoRight.setPower(-0.1);

            setTimeout(
                () -> {
                    bot.hangServoLeft.setPower(0);
                    bot.hangServoRight.setPower(0);
                }, 1500
            );
        }
    }

    protected void internalRun() {
        int counter = 0;

        while (opModeIsActive()) {
            testPlaneLauncher();
            testWheels();
            testHangServos();
            testLifts();

            telemetry.addData(
                    "Wheels",
                    "leftFront (%.2f) leftBack (%.2f) rightFront (%.2f) rightBack (%.2f)",
                    bot.leftFront.getPower(),
                    bot.leftBack.getPower(),
                    bot.rightFront.getPower(),
                    bot.rightBack.getPower()
            );
            telemetry.addData("Plane Launcher Power", planeLauncherPower);
            telemetry.addData("Lift 1", bot.testLift1.getPower());
            telemetry.addData("Lift2", -bot.testLift2.getPower());
            telemetry.addData("Hang Servo Power [real]", "left (%.2f), right (%.2f)", bot.hangServoLeft.getPower(), bot.hangServoRight.getPower());
            telemetry.update();

            counter++;
        }
    }
}