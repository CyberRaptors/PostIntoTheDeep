package lib8812.common.auton.autopilot;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import lib8812.common.rr.drive.SampleMecanumDrive;

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
