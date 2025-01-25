package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.ServoTesterBot;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class ServoTesterRunner extends ITeleOpRunner {
	ServoTesterBot bot = new ServoTesterBot();

	@Override
	protected void internalRun() {
		while (opModeIsActive()) {
			bot.servo.setPosition(
					bot.servo.getPosition()+(gamepad1.inner.right_trigger/1000)-(gamepad1.inner.left_trigger/1000)
			);

			if (gamepad1.inner.dpad_left) bot.servo.setDirection(Servo.Direction.REVERSE);
			if (gamepad1.inner.dpad_right) bot.servo.setDirection(Servo.Direction.FORWARD);

			telemetry.addData("servo", "pos (%.3f)", bot.servo.getPosition());
			telemetry.addData("servo", "direction (%s)", bot.servo.getDirection());
			telemetry.update();
		}
	}

	@Override
	protected IRobot getBot() {
		return bot;
	}
}
