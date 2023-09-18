package org.firstinspires.ftc.teamcode.raptor.auton;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.raptor.auton.autopilot.PositionalConstants;
import org.firstinspires.ftc.teamcode.raptor.auton.autopilot.TrajectoryDelegator;
import org.firstinspires.ftc.teamcode.raptor.auton.autopilot.TrajectoryLists;

@Autonomous(name="Autonomous/Blue/Left/SinglePixel/AutoPilot")
public class BlueLeftSinglePixelAutoPilot extends LinearOpMode {
    public void runOpMode()
    {
        Trajectory[] backdropTrajectories = TrajectoryLists.toBlueBackdrop;
        Trajectory current = backdropTrajectories[0];

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        TrajectoryDelegator delegator = new TrajectoryDelegator(drive, backdropTrajectories);

        drive.followTrajectoryAsync(current);

        while (true) {
            if (delegator.isAtTarget(PositionalConstants.blueBackdrop)) {
                break;
            }

            if (!delegator.shouldChangeTrajectory()) continue;

            drive.breakFollowing();

            current = delegator.getNewTrajectoryPath();

            drive.followTrajectory(current);
        }
    }
}
