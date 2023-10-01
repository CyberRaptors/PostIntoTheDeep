package org.firstinspires.ftc.teamcode.raptor.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.raptor.robot.RaptorRobot;

import lib8812.common.teleop.IDriveableRobot;
import lib8812.common.teleop.ITeleopRunner;

public class RaptorTestRunner extends ITeleopRunner {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() { return bot; };

    private double planeLauncherPower = 0;
    private double hangServoPosition = 0.6;

    public void debugLogOverTelemetry(String message)
    {
        telemetry.addData("dbg", message);
        telemetry.update();
    }

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

    public void testHangServo() {
        if (gamepad2.dpad_right) {
            hangServoPosition+=0.001;
        }

        if (gamepad2.dpad_left) {
            hangServoPosition-=0.001;
        }

        bot.hangServo.setPosition(hangServoPosition);
    }

    protected void internalRun() {
        int counter = 0;

        while (opModeIsActive()) {
            testPlaneLauncher();
            testWheels();
            testHangServo();
            testLifts();

            telemetry.addData("Plane Launcher Power", planeLauncherPower);
            telemetry.addData("Lift 1", bot.testLift1.getPower());
            telemetry.addData("Lift2", -bot.testLift2.getPower());
            telemetry.addData("Hang Servo Position", -bot.hangServo.getPosition());
            telemetry.update();

            counter++;
        }
    }
}