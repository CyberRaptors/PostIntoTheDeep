package lib8812.common.teleop.autopilot;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.apache.commons.math3.geometry.euclidean.twod.Line;

import lib8812.common.auton.autopilot.TrajectoryDelegator;
import lib8812.common.rr.drive.SampleMecanumDrive;
import lib8812.common.rr.trajectorysequence.TrajectorySequence;

public class DestinationDecisionMaker {
    TrajectoryDelegator delegator;
    TrajectorySequence[] currentTrajectories;
    Pose2d currentTarget;
    LinearOpMode opMode;
    SampleMecanumDrive drive;

    public DestinationDecisionMaker(LinearOpMode opMode, SampleMecanumDrive drive) {
        this.opMode = opMode;
        this.drive = drive;
    }

    public void changeGoal(TrajectorySequence[] goalTrajectories, Pose2d target) {
        currentTrajectories = goalTrajectories;
        currentTarget = target;

        delegator = new TrajectoryDelegator(
                opMode, drive, currentTrajectories, currentTarget
        );


    }
}
