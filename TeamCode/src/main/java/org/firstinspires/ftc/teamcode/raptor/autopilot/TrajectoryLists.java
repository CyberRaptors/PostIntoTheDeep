package org.firstinspires.ftc.teamcode.raptor.autopilot;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class TrajectoryLists {
    public static Trajectory[] toBlueBackdrop;
    public static Trajectory[] toRedBackdrop;

    public static void initializeTrajectoryLists(SampleMecanumDrive drive)
    {
        // TODO: initialize trajectory arrays w/ actual trajectories
        toBlueBackdrop = new Trajectory[] { /* ... */ };
        toRedBackdrop = new Trajectory[] { /* ... */ };
    }
}
