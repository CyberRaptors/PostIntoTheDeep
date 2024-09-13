package org.firstinspires.ftc.teamcode.tests.runners;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.rr.drive.SampleMecanumDrive;
import lib8812.common.robot.IDriveableRobot;
import lib8812.common.teleop.ITeleOpRunner;
import static lib8812.common.auton.autopilot.FieldPositions.BLOCK_LENGTH_IN;

public class OdomBlockTesterRunner extends ITeleOpRunner {
    RaptorRobot bot = new RaptorRobot();

    protected IDriveableRobot getBot() { return bot; }
    protected void internalRun() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(new Pose2d());

        while (opModeIsActive())
        {
            if (gamepad1.inner.dpad_up) {
                drive.followTrajectory(
                        drive.trajectoryBuilder(drive.getPoseEstimate())
                                .forward(BLOCK_LENGTH_IN)
                                .build()
                );
            }
            if (gamepad1.inner.dpad_down) {
                drive.followTrajectory(
                        drive.trajectoryBuilder(drive.getPoseEstimate())
                                .back(BLOCK_LENGTH_IN)
                                .build()
                );
            }
            if (gamepad1.inner.dpad_left) {
                drive.followTrajectory(
                        drive.trajectoryBuilder(drive.getPoseEstimate())
                                .strafeLeft(BLOCK_LENGTH_IN)
                                .build()
                );
            }
            if (gamepad1.inner.dpad_right) {
                drive.followTrajectory(
                        drive.trajectoryBuilder(drive.getPoseEstimate())
                                .strafeRight(BLOCK_LENGTH_IN)
                                .build()
                );
            }
        }
    }
}
