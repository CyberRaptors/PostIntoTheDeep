package org.firstinspires.ftc.teamcode.auton.runners;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.teleop.IDriveableRobot;

public class BlueParkRunner extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() { return bot; }

    public void forward(long ms) {
        bot.rightFront.setPower(1);
        bot.leftFront.setPower(1);
        bot.rightBack.setPower(1);
        bot.leftBack.setPower(1);

        sleep(ms);

        bot.rightFront.setPower(0);
        bot.rightBack.setPower(0);
        bot.leftFront.setPower(0);
        bot.leftBack.setPower(0);
    }

    public void back(long ms) {
        bot.rightFront.setPower(-1);
        bot.leftFront.setPower(-1);
        bot.rightBack.setPower(-1);
        bot.leftBack.setPower(-1);

        sleep(ms);

        bot.rightFront.setPower(0);
        bot.rightBack.setPower(0);
        bot.leftFront.setPower(0);
        bot.leftBack.setPower(0);
    }

    public void turnRight(long ms) {
        bot.leftFront.setPower(1);
        bot.leftBack.setPower(1);
        bot.rightFront.setPower(-1);
        bot.rightBack.setPower(-1);

        sleep(ms);

        bot.rightFront.setPower(0);
        bot.rightBack.setPower(0);
        bot.leftFront.setPower(0);
        bot.leftBack.setPower(0);
    }

    public void turnLeft(long ms) {
        bot.leftFront.setPower(-1);
        bot.leftBack.setPower(-1);
        bot.rightFront.setPower(1);
        bot.rightBack.setPower(1);

        sleep(ms);

        bot.rightFront.setPower(0);
        bot.rightBack.setPower(0);
        bot.leftFront.setPower(0);
        bot.leftBack.setPower(0);
    }

    public void strafeLeft(long ms) {
        bot.rightFront.setPower(1);
        bot.rightBack.setPower(-1);
        bot.leftFront.setPower(-1);
        bot.leftBack.setPower(1);

        sleep(ms);

        bot.rightFront.setPower(0);
        bot.rightBack.setPower(0);
        bot.leftFront.setPower(0);
        bot.leftBack.setPower(0);
    }
    public void internalRun(PixelDetectionConstants.PixelPosition pos) {
        sleep(1000);
        pos = objectDetector.getCurrentFeed();

        forward(630);
        sleep(500);

        if (pos == PixelDetectionConstants.PixelPosition.RIGHT) {
            turnRight(300);
            sleep(500);
            bot.tempFlip.setPosition(0.45);
            turnLeft(300);
            sleep(500);
        }
        else if (pos == PixelDetectionConstants.PixelPosition.LEFT) {
            turnLeft(300);
            sleep(500);
            bot.tempFlip.setPosition(0.45);
            turnRight(300);
            sleep(500);
        }
        else if (pos == PixelDetectionConstants.PixelPosition.CENTER) {
            bot.tempFlip.setPosition(0.45);
            sleep(500);
        }

        back(630);
        sleep(500);
        strafeLeft(1750);
    }
}
