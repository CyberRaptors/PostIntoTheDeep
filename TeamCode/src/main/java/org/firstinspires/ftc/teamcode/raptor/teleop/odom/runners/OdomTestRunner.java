package org.firstinspires.ftc.teamcode.raptor.teleop.odom.runners;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.raptor.robot.RaptorRobot;

import lib8812.common.teleop.IDriveableRobot;
import lib8812.common.teleop.ITeleopRunner;

public class OdomTestRunner extends ITeleopRunner {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() { return bot; };

    public void debugLogOverTelemetry(String message)
    {
        telemetry.addData("dbg", message);
        telemetry.update();
    }

    public void driveRobot() {
        bot.rightFront.setPower(-gamepad1.right_stick_y-gamepad1.right_stick_x);
        bot.leftFront.setPower(gamepad1.left_stick_y-gamepad1.left_stick_x);
        bot.rightBack.setPower(-gamepad1.right_stick_y+gamepad1.right_stick_x);
        bot.leftBack.setPower(gamepad1.left_stick_y+gamepad1.left_stick_x);
    }

    public void postPoseData(Pose2d pose)
    {
        telemetry.addData("x", pose.getX());
        telemetry.addData("y", pose.getY());
        telemetry.addData("heading", pose.getHeading());
    }

    protected void internalRun() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        final Pose2d startPose = new Pose2d(0, 0, 0);

        drive.setPoseEstimate(startPose);

        Trajectory trajectory = drive.trajectoryBuilder(startPose) // 10 in forward
            .forward(10)
            .build();

        drive.followTrajectory(trajectory);

        while (opModeIsActive()) {
            drive.updatePoseEstimate();
            postPoseData(drive.getPoseEstimate());
            driveRobot();

            telemetry.update();
        }
    }
}