package org.firstinspires.ftc.teamcode.raptor.auton.detectors;

public class PrimaryRobotDetector implements ObjectDetector<RobotThreat> {
    public RobotThreat getCurrentFeed() {
        return RobotThreat.NONE;
    }
}
