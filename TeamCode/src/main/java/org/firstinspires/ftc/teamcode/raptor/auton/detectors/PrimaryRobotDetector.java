package org.firstinspires.ftc.teamcode.raptor.auton.detectors;

import lib8812.common.auton.IObjectDetector;

public class PrimaryRobotDetector implements IObjectDetector<RobotDetectionConstants.RobotThreat> {
    public RobotDetectionConstants.RobotThreat getCurrentFeed() {
        return RobotDetectionConstants.RobotThreat.NONE;
    }

    public void logInputToTelemetry() { }

    public void destroy() { }

    public void init() { }
}
