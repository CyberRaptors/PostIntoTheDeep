package org.firstinspires.ftc.teamcode.auton.normal.runners;

import org.firstinspires.ftc.teamcode.auton.normal.OdomlessUtil;
import org.firstinspires.ftc.teamcode.robot.MergedRaptorRobot;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class BlueRightParkOnlyRunner extends ITeleOpRunner {
	final MergedRaptorRobot bot = new MergedRaptorRobot();
	final OdomlessUtil util = new OdomlessUtil(bot, this::sleep);

	@Override
	protected IMecanumRobot getBot() {
		return bot;
	}

	@Override
	protected void internalRun() {
		util.init();
		util.moveSync(500, -0.25);
		util.strafeSync(750, -1);
	}
}
