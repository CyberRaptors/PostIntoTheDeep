package org.firstinspires.ftc.teamcode.auton.normal.runners;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.auton.OdomlessUtil;
import lib8812.common.robot.IMecanumRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class RightParkOnlyRunner extends ITeleOpRunner {
	final RaptorRobot bot = new RaptorRobot();
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
