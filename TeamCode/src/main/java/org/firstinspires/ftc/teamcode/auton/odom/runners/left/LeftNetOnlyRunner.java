package org.firstinspires.ftc.teamcode.auton.odom.runners.left;

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

public class LeftNetOnlyRunner extends ITeleOpRunner { // this can impl ITeleOpRunner because no object detection is needed
	static final double STANDARD_TANGENT = Math.PI / 2;

	final static Pose2d initialLeftPose = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, (2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), STANDARD_TANGENT);
	final static Pose2d posForAscent = new Pose2d(FieldConstants.BLOCK_LENGTH_IN+2,0.45*FieldConstants.BLOCK_LENGTH_IN, 0);

	final ActionableRaptorRobot bot = new ActionableRaptorRobot();
	SparkFunOTOSDrive drive;
	MecanumUtil util;

	protected IMecanumRobot getBot() { return bot; }

	Action ascend() {
		return bot.setArmPos(bot.AUTON_ASCENT_ARM_POS);
	}

	protected void internalRun() {
		drive = new SparkFunOTOSDrive(hardwareMap, initialLeftPose);
		util = new MecanumUtil(drive);

		Pose2d endOfNetting = new Pose2d(2.4*FieldConstants.BLOCK_LENGTH_IN, FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);

		Action netAndAscend = new SequentialAction(
				drive.actionBuilder(initialLeftPose)
						.setTangent(STANDARD_TANGENT)
						.lineToY(0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(2*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(STANDARD_TANGENT)
						.lineToY(2.5*FieldConstants.BLOCK_LENGTH_IN)
						.lineToY(0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(endOfNetting.position.x)
						.setTangent(STANDARD_TANGENT)
						.lineToY(2.5*FieldConstants.BLOCK_LENGTH_IN)
						.lineToY(endOfNetting.position.y)
						.build(),
				new ParallelAction(
						ascend(),
						drive.actionBuilder(endOfNetting)
								.splineToSplineHeading(posForAscent, Math.PI)
								.build()
				)

		);

		Action main = new SequentialAction(
				netAndAscend
		);


		Actions.runBlocking(main);

		InteropFields.ARM_END_POS = bot.arm.getPosition();
		InteropFields.LIFT_END_POS = bot.extensionLift.getPosition();
	}
}
 	