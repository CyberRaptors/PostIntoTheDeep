package org.firstinspires.ftc.teamcode.teleop.odom.runners;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.DcMotor;

import lib8812.common.rr.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.telemetrymap.CenterStageFieldComponent;
import lib8812.common.telemetrymap.FieldConstants;
import lib8812.common.telemetrymap.SelectorCenterStageMiniMap;
import lib8812.common.teleop.IDriveableRobot;
import lib8812.common.teleop.ITeleopRunner;

public class OdomTestRunner extends ITeleopRunner {
    // TODO: Change this to include backdrops, etc.
    CenterStageFieldComponent[][] defaultByteMap = new CenterStageFieldComponent[][] {
            new CenterStageFieldComponent[] { CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT },
            new CenterStageFieldComponent[] { CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT },
            new CenterStageFieldComponent[] { CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT },
            new CenterStageFieldComponent[] { CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT },
            new CenterStageFieldComponent[] { CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT },
            new CenterStageFieldComponent[] { CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT }
    };

    CenterStageFieldComponent[][] byteMap = new CenterStageFieldComponent[][] {
            new CenterStageFieldComponent[] { CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT },
            new CenterStageFieldComponent[] { CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT },
            new CenterStageFieldComponent[] { CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT },
            new CenterStageFieldComponent[] { CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT },
            new CenterStageFieldComponent[] { CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT },
            new CenterStageFieldComponent[] { CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT, CenterStageFieldComponent.EMPTY_MAT }
    };
    RaptorRobot bot = new RaptorRobot();
    protected IDriveableRobot getBot() { return bot; };

    public void driveRobot() {
        bot.rightFront.setPower(-gamepad1.right_stick_y-gamepad1.right_stick_x);
        bot.leftFront.setPower(-gamepad1.left_stick_y+gamepad1.left_stick_x);
        bot.rightBack.setPower(-gamepad1.right_stick_y+gamepad1.right_stick_x);
        bot.leftBack.setPower(-gamepad1.left_stick_y-gamepad1.left_stick_x);
    }

    public int[] pinpointRobotOnByteMap(Pose2d pos) {
        double x = pos.getX(),
               y = pos.getY();

        int column = (int) Math.round(x / FieldConstants.BLOCK_LENGTH_IN);
        int row = (int) Math.round(y / FieldConstants.BLOCK_WIDTH_IN);

        return new int[] { column, row };
    }

    public void updateByteMap(Pose2d robotPos) {
        int[] robotPosOnMap = pinpointRobotOnByteMap(robotPos);
        int col = robotPosOnMap[0], row = robotPosOnMap[1];

        for (int i = 0; i < byteMap.length; i++) {
            CenterStageFieldComponent[] byteRow = byteMap[i];

            for (int j = 0; j < byteRow.length; j++) {
                if (i == col && j == row) {
                    byteRow[j] = CenterStageFieldComponent.SELF_ROBOT;
                    continue;
                }

                byteRow[j] = defaultByteMap[i][j];
            }
        }
    }

    public void postPoseData(Pose2d pose)
    {
        telemetry.addData("x", pose.getX());
        telemetry.addData("y", pose.getY());
        telemetry.addData("heading", pose.getHeading());
    }

    protected void internalRun() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        SelectorCenterStageMiniMap miniMap = new SelectorCenterStageMiniMap(opMode, drive);

        final Pose2d startPose = new Pose2d(0, 0, 0);

        drive.setPoseEstimate(startPose);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


//        Trajectory trajectory = drive.trajectoryBuilder(startPose) // 10 in forward
//                .strafeRight(10)
//            .build();
//
//        drive.followTrajectory(trajectory);

        Pose2d curr;

        int counter = 0;

        while (opModeIsActive()) {
            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    )
            );

            drive.update();

            curr = drive.getPoseEstimate();

            postPoseData(curr);

//            driveRobot();

            updateByteMap(curr);
            miniMap.update(byteMap, counter);
            miniMap.printToTelemetry();

            telemetry.update();

            counter++;
        }
    }
}