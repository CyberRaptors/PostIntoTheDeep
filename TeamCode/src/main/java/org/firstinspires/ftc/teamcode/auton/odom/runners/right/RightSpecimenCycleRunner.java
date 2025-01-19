package org.firstinspires.ftc.teamcode.auton.odom.runners.right;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.auton.InteropFields;
import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.auton.MecanumUtil;
import lib8812.common.robot.IMecanumRobot;
import lib8812.common.rr.MecanumDrive;
import lib8812.common.rr.SparkFunOTOSDrive;
import lib8812.common.telemetrymap.FieldConstants;
import lib8812.common.teleop.ITeleOpRunner;

public class RightSpecimenCycleRunner extends ITeleOpRunner { // this can impl ITeleOpRunner because no object detection is needed
	static final double STANDARD_TANGENT = 3 * Math.PI / 2;

	// NOTE: ADD .4 FROM Y_OFFSET FOR AUTON TO ACCOUNT FOR MEET FIELD SETUP

	final static Pose2d initialRightPose = new Pose2d(0.5* FieldConstants.BLOCK_LENGTH_IN, -(2.5*FieldConstants.BLOCK_LENGTH_IN+3.5+0.4), STANDARD_TANGENT);
	final static Pose2d posForSpecimenDrop = new Pose2d(0.45*FieldConstants.BLOCK_LENGTH_IN, -(1.5*FieldConstants.BLOCK_LENGTH_IN-3), STANDARD_TANGENT);
	final static Pose2d posForInitialSpecimenDrop = new Pose2d(0.3*FieldConstants.BLOCK_LENGTH_IN, -(1.5*FieldConstants.BLOCK_LENGTH_IN-3), STANDARD_TANGENT);
	final static Pose2d backupFromChamber = new Pose2d(1.35*FieldConstants.BLOCK_LENGTH_IN, -1.5*FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);
	final static Pose2d specimenPickupPos = new Pose2d(2*FieldConstants.BLOCK_LENGTH_IN+4.5, -(2*FieldConstants.BLOCK_LENGTH_IN-1), STANDARD_TANGENT);
	final static Vector2d posForPark = new Vector2d(2*FieldConstants.BLOCK_LENGTH_IN+4.5, -(2*FieldConstants.BLOCK_LENGTH_IN+10));
	final static Pose2d startPushingFirstSampleFrom = new Pose2d(2*FieldConstants.BLOCK_LENGTH_IN+6, -0.4*FieldConstants.BLOCK_LENGTH_IN, 3*Math.PI/2);
	final static Vector2d pushHungSpecimenUntil = new Vector2d(posForSpecimenDrop.position.x-6, posForSpecimenDrop.position.y+4);
	final static double startPushingSecondSampleXPos = 2.7*FieldConstants.BLOCK_LENGTH_IN;
	final static double pushSamplesUntil = -2.3*FieldConstants.BLOCK_LENGTH_IN;

	Action main;

	final ActionableRaptorRobot bot = new ActionableRaptorRobot();
	SparkFunOTOSDrive drive;
	MecanumUtil util;

	protected IMecanumRobot getBot() { return bot; }

	protected void customInit() {
		drive = new SparkFunOTOSDrive(hardwareMap, initialRightPose);
		util = new MecanumUtil(drive);

		Action prepForPreloadHang = new ParallelAction(
				drive.actionBuilder(initialRightPose)
						.setTangent(STANDARD_TANGENT)
						.strafeToSplineHeading(posForInitialSpecimenDrop.position, posForInitialSpecimenDrop.heading)
						.build(),
				bot.prepareArmForSpecimenHang()
		);

		Action netFirstAndSecond = new SequentialAction(
				new ParallelAction(
						bot.retractArm(),
						drive.actionBuilder(posForSpecimenDrop)
//								.lineToY(-(2*FieldConstants.BLOCK_LENGTH_IN-3))
//								.strafeToSplineHeading(startPushingFirstSampleFrom.position, startPushingFirstSampleFrom.heading)
								.splineToSplineHeading(startPushingFirstSampleFrom, 0)
								.setTangent(STANDARD_TANGENT)
								.lineToY(pushSamplesUntil)
								.lineToY(startPushingFirstSampleFrom.position.y)
								.setTangent(0)
								.lineToX(startPushingSecondSampleXPos)
								.setTangent(STANDARD_TANGENT)
								.lineToY(pushSamplesUntil)
								.strafeToSplineHeading(specimenPickupPos.position, specimenPickupPos.heading)
								.build()
				)
		);

		Action netThird = drive.actionBuilder(posForSpecimenDrop)
				.lineToY(-1.7*FieldConstants.BLOCK_LENGTH_IN)
				.splineToSplineHeading(startPushingFirstSampleFrom, startPushingFirstSampleFrom.heading)
				.setTangent(STANDARD_TANGENT)
				.lineToY(pushSamplesUntil)
				.strafeToSplineHeading(specimenPickupPos.position, specimenPickupPos.heading)
				.build();

		Action pickupSpecimen = new SequentialAction(
				bot.setMaxArmPos(),
				bot.standardFrog(-175),
				bot.armMostlyPerpendicular()
		);

		Action prepForHang = new SequentialAction(
				drive.actionBuilder(specimenPickupPos)
						.strafeToSplineHeading(posForSpecimenDrop.position, posForSpecimenDrop.heading)
						.build(),
				bot.prepareArmForSpecimenHang()
		);

		Action shiftItOver = drive.actionBuilder(posForSpecimenDrop)
				.setTangent(Math.PI/2)
				.lineToY(pushHungSpecimenUntil.y)
				.setTangent(0)
				.lineToX(pushHungSpecimenUntil.x)
				.strafeTo(posForSpecimenDrop.position)
				.build();

		Action returnToOZ = new ParallelAction(
				bot.retractArm(),
				drive.actionBuilder(posForSpecimenDrop)
						.strafeToSplineHeading(specimenPickupPos.position, specimenPickupPos.heading)
						.build()
		);

		Action pickupAndHang = new SequentialAction(
				pickupSpecimen,
				prepForHang,
				bot.hangPreloadStationary()
		);

		Action park = new ParallelAction(
				bot.retractArm(),
				drive.actionBuilder(posForSpecimenDrop)
						.strafeTo(posForPark, (pose2dDual, posePath, v) -> MecanumDrive.PARAMS.maxWheelVel)
						.build()
		);

		main = new SequentialAction(
				bot.clutchPreload(),
				prepForPreloadHang,
				bot.hangPreloadStationary(),
//				shiftItOver,
				netFirstAndSecond,
				pickupAndHang,
//				shiftItOver,
//				returnToOZ,
//				netThird, // -> replace this with a returnToOZ (above) if not going for third
//				pickupAndHang,
//				returnToOZ,
//				pickupAndHang,
//				returnToOZ,
//				pickupAndHang, // could do a last one, but need teammate to allow use to use their specimen for this
//				returnToOZ,
				park
		);
	}

	protected void internalRun() {
		Actions.runBlocking(main);

		InteropFields.ARM_END_POS = bot.arm.getPosition();
		InteropFields.LIFT_END_POS = bot.extensionLift.getPosition();
		bot.deInit();
	}
}
 	