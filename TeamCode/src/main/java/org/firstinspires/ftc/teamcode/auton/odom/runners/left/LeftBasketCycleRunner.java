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

public class LeftBasketCycleRunner extends ITeleOpRunner { // this can impl ITeleOpRunner because no object detection is needed
	static final double STANDARD_TANGENT = Math.PI / 2;

	// NOTE: ADD .4 FROM Y_OFFSET FOR AUTON TO ACCOUNT FOR MEET FIELD SETUP

	final static Pose2d initialLeftPose = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, (2.5*FieldConstants.BLOCK_LENGTH_IN+3.5+1.5), STANDARD_TANGENT);
	final static Pose2d posForAscent = new Pose2d(FieldConstants.BLOCK_LENGTH_IN+1,0.5*FieldConstants.BLOCK_LENGTH_IN, 0);
	final static Pose2d posForHighBasketBackDrop = new Pose2d(2.3*FieldConstants.BLOCK_LENGTH_IN, 2.1*FieldConstants.BLOCK_LENGTH_IN+3+2, 5*Math.PI/4);
	final static Pose2d adjustedPosForHighBasketDropFirst = posForHighBasketBackDrop; //new Pose2d(posForHighBasketBackDrop.position.plus(new Vector2d(0, -1)), posForHighBasketBackDrop.heading);
	final static Pose2d posForFirstPickup = new Pose2d(2*FieldConstants.BLOCK_LENGTH_IN+3.5, 2*FieldConstants.BLOCK_LENGTH_IN-3.2, 3*Math.PI/2);
	final static Pose2d posForSecondPickup = new Pose2d(2.6*FieldConstants.BLOCK_LENGTH_IN+0.7, 2*FieldConstants.BLOCK_LENGTH_IN-2.5, 3*Math.PI/2);
	final static Pose2d posForThirdPickup = new Pose2d(2*FieldConstants.BLOCK_LENGTH_IN+18, 1*FieldConstants.BLOCK_LENGTH_IN+20.3, (3*Math.PI/2)+Math.toRadians(21 ));

	Action main;

	final ActionableRaptorRobot bot = new ActionableRaptorRobot();
	SparkFunOTOSDrive drive;
	MecanumUtil util;

	protected IMecanumRobot getBot() { return bot; }

	protected void customInit() {
		drive = new SparkFunOTOSDrive(hardwareMap, initialLeftPose);
		util = new MecanumUtil(drive);

		Action dropPreloadAndPickupFirst = new SequentialAction(
				new ParallelAction(
						drive.actionBuilder(initialLeftPose)
								.setTangent(4*Math.PI/5)
								.strafeToSplineHeading(posForHighBasketBackDrop.position, posForHighBasketBackDrop.heading)
								.build(),
						bot.clutchPreload(),
						bot.setClawRotatePos(bot.CLAW_ROTATE_FORWARDS),
						bot.prepareArmForBackDrop()
				),
				bot.spitSample(),
				new ParallelAction(
						bot.forceSetExtensionLiftMinPos(),
						drive.actionBuilder(posForHighBasketBackDrop)
								.strafeToSplineHeading(posForFirstPickup.position, posForFirstPickup.heading)
								.build(),
						bot.armMostlyPerpendicular()
				),
				bot.standardFrog()
		);

		Action dropFirstAndPickupSecond = new SequentialAction(
				new ParallelAction(
						bot.armMostlyPerpendicular(),
						bot.clutchPreload()
				),
				util.relocalize(),
				new ParallelAction(
						drive.actionBuilder(posForFirstPickup)
								.setTangent(4*Math.PI/5)
								.strafeToSplineHeading(adjustedPosForHighBasketDropFirst.position, adjustedPosForHighBasketDropFirst.heading)
								.build(),
						bot.asyncBackDropBegin()
				),
				bot.asyncBackDropEnd(),
				new ParallelAction(
						bot.forceSetExtensionLiftMinPos(),
						drive.actionBuilder(adjustedPosForHighBasketDropFirst)
								.strafeToSplineHeading(posForSecondPickup.position, posForSecondPickup.heading)
								.build(),
						bot.armMostlyPerpendicular()
				),
				bot.standardFrog()
		);

		Action dropSecondAndPickupThird = new SequentialAction(
				new ParallelAction(
						bot.armMostlyPerpendicular(),
						bot.clutchPreload()
				),				util.relocalize(),
				new ParallelAction(
						drive.actionBuilder(posForSecondPickup)
								.setTangent(4*Math.PI/5)
								.strafeToSplineHeading(posForHighBasketBackDrop.position, posForHighBasketBackDrop.heading)
								.build(),
						bot.asyncBackDropBegin()
				),
				bot.asyncBackDropEnd(),
				new ParallelAction(
						bot.forceSetExtensionLiftMinPos(),
						drive.actionBuilder(posForHighBasketBackDrop)
								.strafeToSplineHeading(posForThirdPickup.position, posForThirdPickup.heading)
								.build(),
						bot.armMostlyPerpendicular()
				),
				bot.standardFrog()
		);

		Action dropThirdAndAscend = new SequentialAction(
				new ParallelAction(
						bot.armMostlyPerpendicular(),
						bot.clutchPreload()
				),				util.relocalize(),
				new ParallelAction(
						drive.actionBuilder(posForThirdPickup)
								.setTangent(4*Math.PI/5)
								.strafeToSplineHeading(posForHighBasketBackDrop.position, posForHighBasketBackDrop.heading)
								.build(),
						bot.asyncBackDropBegin()
				),
				bot.asyncBackDropEnd(),
				bot.forceSetExtensionLiftMinPos(),
				new ParallelAction(
						drive.actionBuilder(posForHighBasketBackDrop) /*.plus(new Vector2d(-1, 0)), posForHighBasketBackDrop.heading)*/
								.splineToSplineHeading(posForAscent, 3*Math.PI/4) // must use a spline here to avoid hitting side of submersible
								.build(),
						bot.ascend()
				)
		);

		main = new SequentialAction(
				dropPreloadAndPickupFirst,
				dropFirstAndPickupSecond,
				dropSecondAndPickupThird,
				dropThirdAndAscend
		);
	}

	protected void internalRun() { // NOTE: can remove/lower sleeps for speed, also see notes below within actions
		Actions.runBlocking(main);

		InteropFields.ARM_END_POS = bot.arm.getPosition();
		InteropFields.LIFT_END_POS = bot.extensionLift.getPosition();
		bot.deInit();
	}
}
 	