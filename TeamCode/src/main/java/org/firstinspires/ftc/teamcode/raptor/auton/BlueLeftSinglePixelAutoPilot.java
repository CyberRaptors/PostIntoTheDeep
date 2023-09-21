package org.firstinspires.ftc.teamcode.raptor.auton;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.raptor.autopilot.TrajectoryDelegator;
import org.firstinspires.ftc.teamcode.raptor.autopilot.TrajectoryLists;

@Autonomous(name="Autonomous/Blue/Left/SinglePixel/AutoPilot")
public class BlueLeftSinglePixelAutoPilot extends LinearOpMode {
    public void runOpMode()
    {
        /* drop purple pixel here before going to backdrop */

        Trajectory[] backdropTrajectories = TrajectoryLists.toBlueBackdrop;

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        TrajectoryDelegator delegator = new TrajectoryDelegator(drive, backdropTrajectories);

        delegator.pathFindToTarget();
    }
}
