package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.robot.IDriveableRobot;
import lib8812.common.teleop.ITeleopRunner;

public class RaptorMainRunner extends ITeleopRunner {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() { return bot; };

    protected void internalRun() { };
}