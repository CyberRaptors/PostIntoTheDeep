package org.firstinspires.ftc.teamcode.auton.odom.runners;

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

public class LeftBasketCycleRunner extends ITeleOpRunner { // this can impl ITeleOpRunner because no object detection is needed
	static final double STANDARD_TANGENT = Math.PI / 2;

	final static Pose2d initialLeftPose = new Pose2d(1.5* FieldConstants.BLOCK_LENGTH_IN, (2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), STANDARD_TANGENT);
	final static Pose2d posForAscent = new Pose2d(FieldConstants.BLOCK_LENGTH_IN+2,0.45*FieldConstants.BLOCK_LENGTH_IN, 0);
	final static Pose2d posForHighBasketBackDrop = new Pose2d(2.3*FieldConstants.BLOCK_LENGTH_IN, 2.3*FieldConstants.BLOCK_LENGTH_IN, 5*Math.PI/4);
	final static Pose2d posForFirstPickup = new Pose2d(2*FieldConstants.BLOCK_LENGTH_IN, 2*FieldConstants.BLOCK_LENGTH_IN, 3*Math.PI/2);
	final static Pose2d posForSecondPickup = new Pose2d(2.5*FieldConstants.BLOCK_LENGTH_IN, 2*FieldConstants.BLOCK_LENGTH_IN, 3*Math.PI/2);
	final static Pose2d posForThirdPickup = new Pose2d(2.5*FieldConstants.BLOCK_LENGTH_IN, 2*FieldConstants.BLOCK_LENGTH_IN, 5*Math.PI/3);

	final ActionableRaptorRobot bot = new ActionableRaptorRobot();
	SparkFunOTOSDrive drive;
	MecanumUtil util;

	protected IMecanumRobot getBot() { return bot; }

	protected void internalRun() {
		drive = new SparkFunOTOSDrive(hardwareMap, initialLeftPose);
		util = new MecanumUtil(drive);

		Action dropPreloadAndPickupFirst = new SequentialAction(
				drive.actionBuilder(initialLeftPose)
						.setTangent(4*Math.PI/5)
						.strafeToSplineHeading(posForHighBasketBackDrop.position, posForHighBasketBackDrop.heading)
						.build(),
				bot.prepareArmForBackDrop(),
				bot.spitSample(),
				drive.actionBuilder(posForHighBasketBackDrop)
						.strafeToSplineHeading(posForFirstPickup.position, posForFirstPickup.heading)
						.build(),
				bot.standardFrog()
		);

		Action dropFirstAndPickupSecond = new SequentialAction(
				drive.actionBuilder(posForFirstPickup)
						.setTangent(4*Math.PI/5)
						.strafeToSplineHeading(posForHighBasketBackDrop.position, posForHighBasketBackDrop.heading)
						.build(),
				bot.prepareArmForBackDrop(),
				bot.spitSample(),
				drive.actionBuilder(posForHighBasketBackDrop)
						.strafeToSplineHeading(posForSecondPickup.position, posForSecondPickup.heading)
						.build(),
				bot.standardFrog()
		);

		Action dropSecondAndPickupThird = new SequentialAction(
				drive.actionBuilder(posForSecondPickup)
						.setTangent(4*Math.PI/5)
						.strafeToSplineHeading(posForHighBasketBackDrop.position, posForHighBasketBackDrop.heading)
						.build(),
				bot.prepareArmForBackDrop(),
				bot.spitSample(),
				drive.actionBuilder(posForHighBasketBackDrop)
						.strafeToSplineHeading(posForThirdPickup.position, posForThirdPickup.heading)
						.build(),
				bot.standardFrog()
		);

		Action dropThirdAndAscend = new SequentialAction(
				drive.actionBuilder(posForThirdPickup)
						.setTangent(4*Math.PI/5)
						.strafeToSplineHeading(posForHighBasketBackDrop.position, posForHighBasketBackDrop.heading)
						.build(),
				bot.prepareArmForBackDrop(),
				bot.spitSample(),
				new ParallelAction(
						drive.actionBuilder(posForHighBasketBackDrop)
								.splineToSplineHeading(posForAscent, 3*Math.PI/4) // must use a spline here to avoid hitting side of submersible
								.build(),
						bot.ascend()
				)
		);

		Action main = new SequentialAction(
				dropPreloadAndPickupFirst,
				dropFirstAndPickupSecond,
				dropSecondAndPickupThird,
				dropThirdAndAscend
		);

		Actions.runBlocking(main);

		InteropFields.ARM_END_POS = bot.arm.getPosition();
		InteropFields.LIFT_END_POS = bot.extensionLift.getPosition();
	}
}
 	