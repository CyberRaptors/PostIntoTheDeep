package lib8812.common.auton.autopilot;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.apache.commons.math3.analysis.function.Abs;

import lib8812.common.auton.IObjectDetector;
import lib8812.common.builtindetectors.RobotDetectionConstants;
import lib8812.common.rr.drive.SampleMecanumDrive;
import lib8812.common.rr.trajectorysequence.TrajectorySequence;

public class TrajectoryDelegator {
    IObjectDetector<RobotDetectionConstants.RobotThreat> detector;
    SampleMecanumDrive drive;
    TrajectorySequence[] trajectories;
    Pose2d target;

    public TrajectoryDelegator(LinearOpMode opMode, SampleMecanumDrive inputDrive, TrajectorySequence[] trajectoryList, Pose2d target) {
        drive = inputDrive;
        trajectories = trajectoryList;
//        detector = new PrimaryRobotDetector(opMode);
        this.target = target;
    }

    // TODO: fix function so that it doesnt fire more than once for the same robot
    //  [e.g. it shouldn't fire again if a backup trajectory has already been calculated to avoid collision with the robot that it detects]
    public boolean shouldChangeTrajectory() {
        return detector.getCurrentFeed() == RobotDetectionConstants.RobotThreat.URGENT;
    }

    public TrajectorySequence getNewTrajectoryPath() {
        TrajectorySequence backup = calculateClosestBackupTrajectory();

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
                .addTemporalMarker(() -> {
                    drive.breakFollowing();
                    drive.followTrajectorySequence(backup);
                })
                .build();
    }

    public boolean isAtTarget() {
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

    TrajectorySequence calculateClosestBackupTrajectory() {
        double leastDist = Double.MAX_VALUE;
        TrajectorySequence closest = null;

        for (TrajectorySequence trajectory : trajectories) {
            Pose2d start = trajectory.start();
            Pose2d curr = drive.getPoseEstimate();

            double dist = Math.abs(start.getY()-curr.getY())+Math.abs(start.getX()-curr.getX());

            if (dist < leastDist) {
                leastDist = dist;
                closest = trajectory;
            }
        }

        return closest;
    }

    public void pathFindToTarget() {
        TrajectorySequence current = trajectories[0];

        drive.followTrajectorySequenceAsync(current);

        while (drive.isBusy()) {
            drive.update();

            if (!shouldChangeTrajectory()) continue;

            drive.breakFollowing();

            current = getNewTrajectoryPath();

            drive.followTrajectorySequenceAsync(current);
        }
    }
}
