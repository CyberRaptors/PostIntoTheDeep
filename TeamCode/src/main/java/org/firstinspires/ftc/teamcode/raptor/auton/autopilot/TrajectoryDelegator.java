package org.firstinspires.ftc.teamcode.raptor.auton.autopilot;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.raptor.auton.detectors.RobotDetector;
import org.firstinspires.ftc.teamcode.raptor.auton.detectors.RobotThreat;

public class TrajectoryDelegator {
    final RobotDetector detector = new RobotDetector();
    SampleMecanumDrive drive;
    Trajectory[] trajectories;

    public TrajectoryDelegator(SampleMecanumDrive inputDrive, Trajectory[] trajectoryList) {
        drive = inputDrive;
        trajectories = trajectoryList;
    }
    public boolean shouldChangeTrajectory() {
        return detector.getCurrentFeed() == RobotThreat.URGENT;
    }
    public Trajectory getNewTrajectoryPath() {
        Trajectory backup = calculateClosestBackupTrajectory();

        return backup; // TODO: write trajectory to reach this new backup trajectory
    }

    public boolean isAtTarget(Pose2d target) {
        Pose2d curr = drive.getPoseEstimate();

        return
            (curr.getX() == target.getX()) &&
            (curr.getY() == target.getY()) &&
            (curr.getHeading() == target.getHeading());
    }

    Trajectory calculateClosestBackupTrajectory() {
        return trajectories[0]; // TODO: write actual code to calculate closest backup trajectory to robot
    }
}
