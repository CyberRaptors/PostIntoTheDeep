package org.firstinspires.ftc.teamcode.auton.odom.runners.right;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
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

	final static Pose2d initialRightPose = new Pose2d(0.45 * FieldConstants.BLOCK_LENGTH_IN, -(2.5*FieldConstants.BLOCK_LENGTH_IN+3.5+0.4), STANDARD_TANGENT);
	final static Pose2d posForSpecimenDropSpliner = new Pose2d(0.45*FieldConstants.BLOCK_LENGTH_IN, -(1.5*FieldConstants.BLOCK_LENGTH_IN+5), STANDARD_TANGENT);
	final static Pose2d posForSpecimenDrop = new Pose2d(0.45*FieldConstants.BLOCK_LENGTH_IN, -(1.5*FieldConstants.BLOCK_LENGTH_IN-5), STANDARD_TANGENT);
	final static Pose2d posForSpecimenDropBackup = new Pose2d(0.45*FieldConstants.BLOCK_LENGTH_IN, -(1.5*FieldConstants.BLOCK_LENGTH_IN+5), STANDARD_TANGENT);
	final static Pose2d posForInitialSpecimenDrop = new Pose2d(0.2*FieldConstants.BLOCK_LENGTH_IN, -(1.5*FieldConstants.BLOCK_LENGTH_IN-8), STANDARD_TANGENT);
	final static Pose2d posForInitialSpecimenDropBackup = new Pose2d(0.2*FieldConstants.BLOCK_LENGTH_IN, -(1.5*FieldConstants.BLOCK_LENGTH_IN-2), STANDARD_TANGENT);
	final static Pose2d backupFromChamber = new Pose2d(1.35*FieldConstants.BLOCK_LENGTH_IN, -1.5*FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);
	final static Pose2d specimenPickupPosStart = new Pose2d(2*FieldConstants.BLOCK_LENGTH_IN+4.5, -(2*FieldConstants.BLOCK_LENGTH_IN+7), Math.PI / 2);
	final static Pose2d specimenPickupPosEnd = new Pose2d(specimenPickupPosStart.position.minus(new Vector2d(0, 7)), specimenPickupPosStart.heading);
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
				bot.setExtensionLiftPos(150),
				new SequentialAction(
						bot.prepareArmForSpecimenHang(),
						bot.forceSetExtensionLiftMinPos()
				),
				drive.actionBuilder(initialRightPose)
						.setTangent(STANDARD_TANGENT)
						.strafeToSplineHeading(posForInitialSpecimenDrop.position, posForInitialSpecimenDrop.heading)
						.build()
		);

		Action netFirst = new SequentialAction(
				bot.idleIntakes(),
				new ParallelAction(
						new SequentialAction(
								new SleepAction(0.5),
								bot.runIntakesOut(),
								new SleepAction(1),
								bot.fastHangSpecimenEnd()
						),
						drive.actionBuilder(posForInitialSpecimenDrop)
								.splineToSplineHeading(startPushingFirstSampleFrom, 0)
								.setTangent(STANDARD_TANGENT)
								.lineToY(pushSamplesUntil)
								.strafeToSplineHeading(specimenPickupPosStart.position, specimenPickupPosStart.heading)
								.build()
				)
		);

		Action netThird = drive.actionBuilder(posForSpecimenDrop)
				.lineToY(-1.7*FieldConstants.BLOCK_LENGTH_IN)
				.splineToSplineHeading(startPushingFirstSampleFrom, startPushingFirstSampleFrom.heading)
				.setTangent(STANDARD_TANGENT)
				.lineToY(pushSamplesUntil)
				.strafeToSplineHeading(specimenPickupPosStart.position, specimenPickupPosStart.heading)
				.build();


		Action prepForHang = new SequentialAction(
				drive.actionBuilder(specimenPickupPosEnd)
						.strafeToSplineHeading(posForSpecimenDropSpliner.position, posForSpecimenDropSpliner.heading)
						.build(),
				bot.prepareArmForSpecimenHang(),
				drive.actionBuilder(posForSpecimenDropSpliner)
						.setTangent(Math.PI / 2)
						.lineToY(posForSpecimenDrop.position.y)
						.build()
		);

		Action backupIntoWallAndStartPickup = new SequentialAction(
				new InstantAction(() -> {
					bot.intakeLarge.setPower(bot.INTAKE_LARGE_IN_DIRECTION);
					bot.intakeSmall.setPower(bot.INTAKE_SMALL_IN_DIRECTION);
				}),
				bot.drive.actionBuilder(specimenPickupPosStart) // move backwards
						.setTangent(Math.PI/2)
						.lineToY(specimenPickupPosStart.position.y-5)
						.build()
		);

		Action endPickup = new SequentialAction(
				new SleepAction(0.2),
				new InstantAction(() -> {
					bot.intakeLarge.setPower(0);
					bot.intakeSmall.setPower(0);
				})
		);

		Action inchForwardAndRaiseArm = new SequentialAction(
				bot.drive.actionBuilder(new Pose2d(specimenPickupPosStart.position.minus(new Vector2d(0, 5)), specimenPickupPosStart.heading)) // inch forwards
						.setTangent(Math.PI/2)
						.lineToY(specimenPickupPosEnd.position.y)
						.build(),
				bot.setArmPos(bot.ARM_PICKUP_FROM_BACK_WALL+150)
		);

		Action pickupSpecimenFromFieldWall = new SequentialAction(
				bot.setArmPos(bot.ARM_PICKUP_FROM_BACK_WALL),
				backupIntoWallAndStartPickup,
				endPickup,
				inchForwardAndRaiseArm
		);

		Action shiftItOver = drive.actionBuilder(posForSpecimenDrop)
				.setTangent(Math.PI/2)
				.lineToY(pushHungSpecimenUntil.y)
				.setTangent(0)
				.lineToX(pushHungSpecimenUntil.x)
				.strafeTo(posForSpecimenDrop.position)
				.build();

		Action returnToOZAndHangFast = new SequentialAction(
				drive.actionBuilder(posForSpecimenDropBackup)
						.afterDisp(1.5, bot.fastHangSpecimenEnd())
						.afterDisp(5, new SequentialAction(
								bot.setArmPos(bot.ARM_PICKUP_FROM_BACK_WALL),
								bot.runIntakesIn()
						))
						.strafeToSplineHeading(specimenPickupPosEnd.position, specimenPickupPosEnd.heading)
						.build(),
				bot.idleIntakes(),
				bot.setArmPos(bot.ARM_PICKUP_FROM_BACK_WALL+150),
				prepForHang,
				bot.fastHangSpecimenBegin(),
				bot.fastHangSpecimenWrap(
						drive.actionBuilder(posForSpecimenDrop)
								.setTangent(Math.PI / 2)
								.lineToY(posForSpecimenDropBackup.position.y)
				)
		);

		Action pickupAndHang = new SequentialAction(
				pickupSpecimenFromFieldWall,
				prepForHang,
				bot.fastHangSpecimenBegin(),
				bot.fastHangSpecimenWrap(
						drive.actionBuilder(posForSpecimenDrop)
								.setTangent(Math.PI / 2)
								.lineToY(posForSpecimenDropBackup.position.y)
				)
		);

		Action park = new ParallelAction(
				bot.retractArm(),
				drive.actionBuilder(posForSpecimenDropBackup)
						.strafeTo(posForPark, (pose2dDual, posePath, v) -> MecanumDrive.PARAMS.maxWheelVel)
						.build()
		);

		main = new SequentialAction(
				bot.clutchPreload(),
				prepForPreloadHang,
				bot.fastHangSpecimenBegin(),
//				shiftItOver,
				netFirst,
				pickupAndHang,
				returnToOZAndHangFast,
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
 	