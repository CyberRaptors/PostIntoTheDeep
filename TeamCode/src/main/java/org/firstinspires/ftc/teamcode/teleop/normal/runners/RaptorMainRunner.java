package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;


import lib8812.common.robot.IDriveableRobot;
import lib8812.common.robot.WheelPowers;
import lib8812.common.teleop.ITeleopRunner;
import lib8812.common.teleop.TeleOpUtils;

public class RaptorMainRunner extends ITeleopRunner {
    RaptorRobot bot = new RaptorRobot();
    private final WheelPowers wheelWeights = new WheelPowers(1, 1, 1, 1);

    protected IDriveableRobot getBot() { return bot; }

    public void runWheels() {
        double correctedRightY = TeleOpUtils.fineTuneInput(gamepad1.inner.right_stick_y);
        double correctedRightX = TeleOpUtils.fineTuneInput(gamepad1.inner.right_stick_x);
        double correctedLeftY = TeleOpUtils.fineTuneInput(gamepad1.inner.left_stick_y);
        double correctedLeftX = TeleOpUtils.fineTuneInput(gamepad1.inner.left_stick_x);

        WheelPowers correctedWheelPowers = new WheelPowers(
                (correctedLeftY-correctedLeftX),
                (correctedLeftY+correctedLeftX),
                (-correctedRightY-correctedRightX),
                (-correctedRightY+correctedRightX)
        );

        correctedWheelPowers.applyTo(bot, wheelWeights);
    }

    protected void internalRun() {
        int counter = 0;

        while (opModeIsActive()) {
            runWheels();

            telemetry.update();

            counter++;
        }
    }
}