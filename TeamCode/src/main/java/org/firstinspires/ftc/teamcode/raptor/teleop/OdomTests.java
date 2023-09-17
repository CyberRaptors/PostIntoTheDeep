package org.firstinspires.ftc.teamcode.raptor.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.raptor.robot.RaptorRobot;

@TeleOp(name="Odometry/Tests", group="Linear Opmode")
public class OdomTests extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    RaptorRobot bot = new RaptorRobot();

    public void debugLogOverTelemetry(String message)
    {
        telemetry.addData("dbg", message);
        telemetry.update();
    }

    public void initOpMode() {
        this.bot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();
    }

    public void postPoseData(Pose2d pose)
    {
        telemetry.addData("x", pose.getX());
        telemetry.addData("y", pose.getY());
        telemetry.addData("heading", pose.getHeading());
    }

    @Override
    public void runOpMode() {
        initOpMode();

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        final Pose2d startPose = new Pose2d(0, 0, 0);

        drive.setPoseEstimate(startPose);

        Trajectory trajectory = drive.trajectoryBuilder(startPose) // 10 in forward
            .forward(10)
            .build();

        while (opModeIsActive()) {
            postPoseData(drive.getPoseEstimate());
            
            telemetry.update();
        }
    }
}