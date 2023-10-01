package org.firstinspires.ftc.teamcode.raptor.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.raptor.robot.RaptorRobot;
import lib8812.common.teleop.ITeleopRunner;

public class RaptorMainRunner extends ITeleopRunner {
    RaptorRobot bot = new RaptorRobot();

    public void debugLogOverTelemetry(String message)
    {
        telemetry.addData("dbg", message);
        telemetry.update();
    }

    protected void internalRun() { };
}