package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.robot.REVRaptorRobot;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class REVRaptorRunner extends ITeleOpRunner {

	REVRaptorRobot bot = new REVRaptorRobot();

	@Override
	protected void internalRun() {
		while (opModeIsActive()) {
			bot.rightDrive.setPower(gamepad1.inner.right_stick_y);
			bot.leftDrive.setPower(gamepad1.inner.left_stick_y);


			telemetry.addData("left drive", "power (%.2f)", bot.leftDrive.getPower());
			telemetry.addData("right drive", "power (%.2f)", bot.rightDrive.getPower());
			telemetry.update();
		}
	}

	@Override
	protected IRobot getBot() {
		return bot;
	}
}
