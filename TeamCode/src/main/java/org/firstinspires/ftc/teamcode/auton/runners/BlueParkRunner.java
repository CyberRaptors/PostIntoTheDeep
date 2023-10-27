package org.firstinspires.ftc.teamcode.auton.runners;

import org.firstinspires.ftc.teamcode.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.auton.IAutonomousRunner;
import lib8812.common.teleop.IDriveableRobot;

public class BlueParkRunner extends IAutonomousRunner<PixelDetectionConstants.PixelPosition> {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() { return bot; }

    public void strafe(long ms) {
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
        strafe(10000);
    }
}
