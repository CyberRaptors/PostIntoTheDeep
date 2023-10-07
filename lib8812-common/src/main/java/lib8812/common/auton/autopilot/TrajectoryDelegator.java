package lib8812.common.auton.autopilot;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import lib8812.common.auton.IObjectDetector;
import lib8812.common.builtindetectors.PrimaryRobotDetector;
import lib8812.common.builtindetectors.RobotDetectionConstants;
import lib8812.common.rr.drive.SampleMecanumDrive;
import lib8812.common.rr.trajectorysequence.TrajectorySequence;

public class TrajectoryDelegator {
    IObjectDetector<RobotDetectionConstants.RobotThreat> detector;
    SampleMecanumDrive drive;
    Trajectory[] trajectories;

    public TrajectoryDelegator(LinearOpMode opMode, SampleMecanumDrive inputDrive, Trajectory[] trajectoryList) {
        drive = inputDrive;
        trajectories = trajectoryList;
        detector = new PrimaryRobotDetector(opMode);
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

        double currX = curr.getX();
        double currY = curr.getY();
        double currHead = curr.getHeading();

        double targetX = target.getX();
        double targetY = target.getY();
        double targetHead = target.getHeading();

        return
            ((currX > targetX - 0.01) && (currX < targetX + 0.01)) &&
            ((currY > targetY - 0.01) && (currY < targetY + 0.01)) &&
            ((currHead > targetHead - 0.01) && (currHead < targetHead + 0.01));
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
