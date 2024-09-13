package org.firstinspires.ftc.teamcode.teleop.odom.runners;


import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.robot.IDriveableRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class OdomMainRunner extends ITeleOpRunner {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() { return bot; }

    protected void internalRun() {

        while (opModeIsActive()) {

        }
    }
}