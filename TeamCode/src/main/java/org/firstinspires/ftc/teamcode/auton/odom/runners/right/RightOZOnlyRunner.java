package org.firstinspires.ftc.teamcode.auton.odom.runners.right;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.auton.InteropFields;
import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.auton.MecanumUtil;
import lib8812.common.robot.IMecanumRobot;
import lib8812.common.rr.SparkFunOTOSDrive;
import lib8812.common.telemetrymap.FieldConstants;
import lib8812.common.teleop.ITeleOpRunner;

public class RightOZOnlyRunner extends ITeleOpRunner { // this can impl ITeleOpRunner because no object detection is needed
	static final double STANDARD_TANGENT = Math.PI / 2;


	final static Pose2d initialRightPose = new Pose2d(0.5*FieldConstants.BLOCK_LENGTH_IN, -(2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), STANDARD_TANGENT);
	final static Pose2d backupFromChamber = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, -1.5*FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);


	final ActionableRaptorRobot bot = new ActionableRaptorRobot();
	SparkFunOTOSDrive drive;
	MecanumUtil util;

	protected IMecanumRobot getBot() { return bot; }

	protected void internalRun() {
		drive = new SparkFunOTOSDrive(hardwareMap, initialRightPose);
		util = new MecanumUtil(drive);

		Action netAndAscend = new SequentialAction(
				new ParallelAction(
						drive.actionBuilder(initialRightPose)
								.setTangent(Math.PI/2)
								.splineToSplineHeading(backupFromChamber, Math.PI/2)
								.build()
				),

				drive.actionBuilder(backupFromChamber)
						.setTangent(STANDARD_TANGENT)
						.lineToY(-0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(2*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(STANDARD_TANGENT)
						.lineToY(-2.5*FieldConstants.BLOCK_LENGTH_IN)
						.lineToY(-0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(2.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(STANDARD_TANGENT)
						.lineToY(-2.5*FieldConstants.BLOCK_LENGTH_IN)
						.build()

		);

		Action main = new SequentialAction(
				netAndAscend
		);

		Actions.runBlocking(main);

		InteropFields.ARM_END_POS = bot.arm.getPosition();
		InteropFields.LIFT_END_POS = bot.extensionLift.getPosition();
	}
}
 	