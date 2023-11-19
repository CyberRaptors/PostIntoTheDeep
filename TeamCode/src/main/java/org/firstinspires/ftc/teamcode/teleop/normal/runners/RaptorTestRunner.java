package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.teleop.IDriveableRobot;
import lib8812.common.teleop.ITeleopRunner;
import lib8812.common.teleop.TeleOpUtils;

public class RaptorTestRunner extends ITeleopRunner {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() { return bot; };

    public void testLifts() {
        bot.testLift1.setPower(-gamepad2.right_stick_y);
        bot.testLift2.setPower(gamepad2.right_stick_y);
    }

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

    public void testClaw() {
        if (gamepad2.right_bumper) {
            bot.claw.setPosition(bot.CLAW_OPEN);
        }

        if (gamepad2.left_bumper) {
            bot.claw.setPosition(bot.CLAW_CLOSED);
        }
    }

    public void testPlaneShooter() {
        if (gamepad2.dpad_up) {
            bot.planeShooter.setPosition(bot.PLANE_SHOT);
        }
        if (gamepad2.dpad_down) {
            bot.planeShooter.setPosition(bot.PLANE_READY);
        }
    }

    public void testArmServos(int counter) {
        if (gamepad2.dpad_right) {
            bot.clawRotate1.setPower(0.2);
            bot.clawRotate2.setPower(-0.2);
        }
        if (gamepad2.dpad_left) {
            bot.clawRotate1.setPower(-0.2);
            bot.clawRotate2.setPower(0.2);
        }
        else if (counter % 50 == 0)
        {
            bot.clawRotate1.setPower(0);
            bot.clawRotate2.setPower(0);
        }
    }

    public void testArm() {
        bot.arm.setPower(gamepad2.left_stick_y);
    }

    public void runIntakeSequences() {
        if (gamepad2.y) {
            bot.arm.setPower(-1);
            bot.testLift1.setPower(1);
            bot.testLift2.setPower(-1);
            sleep(1000);
            bot.testLift1.setPower(0);
            bot.testLift2.setPower(-0);

            bot.arm.setPower(0.2);
            bot.clawRotate1.setPower(0.5);
            bot.clawRotate2.setPower(-0.5);
            sleep(1000);
        }

        if (gamepad2.b)
        {
            bot.arm.setPower(1);
            bot.testLift1.setPower(1);
            bot.testLift2.setPower(-1);
            bot.clawRotate1.setPower(0.1);
            bot.clawRotate2.setPower(-0.1);
            sleep(750);
            bot.testLift1.setPower(0);
            bot.testLift2.setPower(-0);

            bot.clawRotate1.setPower(-0);
            bot.clawRotate2.setPower(0);
        }
    }

    protected void internalRun() {
        int counter = 0;

        while (opModeIsActive()) {
            double[] realWheelInputPower = testWheelsAndReturnRealInputPower();

            testLifts();
            testClaw();
            testPlaneShooter();
            testArmServos(counter);
            testArm();
            runIntakeSequences();


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
                    bot.leftFront.getPower(), realWheelInputPower[0]-bot.leftFront.getPower(),
                    bot.leftBack.getPower(), realWheelInputPower[1]-bot.leftBack.getPower(),
                    bot.rightFront.getPower(), realWheelInputPower[2]-bot.rightFront.getPower(),
                    bot.rightBack.getPower(), realWheelInputPower[3]-bot.rightBack.getPower()
            );
            telemetry.addData("Plane Launcher", bot.planeShooter.getPosition() < bot.PLANE_READY ? "SHOT" : "READY");
            telemetry.addData("Claw", bot.claw.getPosition() == bot.CLAW_OPEN ? "OPEN" : "CLOSED");
            telemetry.addData("Lift 1", "power (%.2f)", bot.testLift1.getPower());
            telemetry.addData("Lift2", "power (%.2f)",-bot.testLift2.getPower());
            telemetry.addData("Claw Rotate Servo Powers", "one (%.2f) two (%.2f)", bot.clawRotate1.getPower(), bot.clawRotate2.getPower());
            telemetry.addData("Arm", "power (%.2f)", bot.arm.getPower());
            telemetry.update();

            counter++;
        }
    }
}