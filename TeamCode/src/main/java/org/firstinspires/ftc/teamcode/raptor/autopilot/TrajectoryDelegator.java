package org.firstinspires.ftc.teamcode.raptor.autopilot;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.raptor.auton.detectors.PrimaryRobotDetector;
import org.firstinspires.ftc.teamcode.raptor.auton.detectors.RobotDetectionConstants;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class TrajectoryDelegator {
    final PrimaryRobotDetector detector = new PrimaryRobotDetector();
    SampleMecanumDrive drive;
    Trajectory[] trajectories;

    public TrajectoryDelegator(SampleMecanumDrive inputDrive, Trajectory[] trajectoryList) {
        drive = inputDrive;
        trajectories = trajectoryList;
    }

    // TODO: fix function so that it doesnt fire more than once for the same robot
    //  [e.g. it shouldn't fire again if a backup trajectory has already been calculated to avoid collision with the robot that it detects]
    public boolean shouldChangeTrajectory() {
        return detector.getCurrentFeed() == RobotDetectionConstants.RobotThreat.URGENT;
    }
    public TrajectorySequence getNewTrajectoryPath() {
        Trajectory backup = calculateClosestBackupTrajectory();

        Pose2d dest = backup.start();

        Pose2d curr = drive.getPoseEstimate();

        double xdiff = dest.getX() - curr.getX();
        double ydiff = dest.getY() - curr.getY();
        double headdiff = dest.getHeading() - curr.getHeading();

        TrajectoryBuilder builder;
        Trajectory toBackup;

        if (xdiff > 0) {
            builder = drive.trajectoryBuilder(curr).strafeLeft(xdiff);
        }
        else {
            builder = drive.trajectoryBuilder(curr).strafeRight(xdiff);
        }

        if (ydiff > 0) {
            builder.back(ydiff);
        }
        else {
            builder.forward(-ydiff);
        }

        toBackup = builder.build();

        return drive.trajectorySequenceBuilder(curr)
                .addTrajectory(toBackup)
                .turn(headdiff)
                .addTrajectory(backup)
                .build();
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

    public void pathFindToTarget() {
        TrajectorySequence current = drive
                .trajectorySequenceBuilder(
                        drive.getPoseEstimate()
                ).addTrajectory(
                        trajectories[0]
                ).build();

        drive.followTrajectorySequenceAsync(current);

        while (true) {
            drive.update();

            if (isAtTarget(PositionalConstants.blueBackdrop)) {
                break;
            }

            if (!shouldChangeTrajectory()) continue;

            drive.breakFollowing();

            current = getNewTrajectoryPath();

            drive.followTrajectorySequenceAsync(current);
        }
    }
}
