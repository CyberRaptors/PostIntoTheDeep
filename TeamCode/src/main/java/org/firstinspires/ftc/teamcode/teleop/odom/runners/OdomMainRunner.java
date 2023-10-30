package org.firstinspires.ftc.teamcode.teleop.odom.runners;


import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.teleop.IDriveableRobot;
import lib8812.common.teleop.ITeleopRunner;

public class OdomMainRunner extends ITeleopRunner {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() { return bot; };

    protected void internalRun() {

        while (opModeIsActive()) {

        }
    }
}