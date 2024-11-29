package org.firstinspires.ftc.teamcode.auton.odom.runners;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.auton.InteropFields;
import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.auton.MecanumUtil;
import lib8812.common.robot.IMecanumRobot;
import lib8812.common.rr.SparkFunOTOSDrive;
import lib8812.common.telemetrymap.FieldConstants;
import lib8812.common.teleop.ITeleOpRunner;

public class LeftSingleSpecimenRunner extends ITeleOpRunner { // this can impl ITeleOpRunner because no object detection is needed
	static final double STANDARD_TANGENT = Math.PI / 2;

	final static Pose2d initialLeftPose = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, (2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), STANDARD_TANGENT);
	final static Pose2d posForSpecimenDrop = new Pose2d(0.457*FieldConstants.BLOCK_LENGTH_IN, 1.5*FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);
	final static Pose2d backupFromChamber = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, 1.5*FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);
	final static Pose2d netZone = new Pose2d(2.4*FieldConstants.BLOCK_LENGTH_IN, 2.4*FieldConstants.BLOCK_LENGTH_IN, 5*Math.PI/4);
	final static Pose2d posForAscent = new Pose2d(FieldConstants.BLOCK_LENGTH_IN+2,0.43*FieldConstants.BLOCK_LENGTH_IN, 0);


	final ActionableRaptorRobot bot = new ActionableRaptorRobot();
	SparkFunOTOSDrive drive;
	MecanumUtil util;

	protected IMecanumRobot getBot() { return bot; }

	Action prepareArmForHang() {
		return new ParallelAction(
			bot.setArmPos(bot.BACKWARDS_HIGH_CHAMBER_ARM_POS),
			bot.setClawRotatePos(bot.CLAW_ROTATE_FORWARDS)
		);
	}

	Action retractArm() {
		return bot.setArmPos(bot.arm.minPos);
	}

	Action hangPreloadStationary() {
		return new SequentialAction(
				new InstantAction(() -> {
					/*  clutch the specimen */
					bot.intakeSmall.setPower(bot.INTAKE_SMALL_IN_DIRECTION);
					bot.intakeLarge.setPower(bot.INTAKE_LARGE_IN_DIRECTION);
				}),
				/* hook the specimen onto the high chamber and wait for at least 0.5 sec */
				bot.setExtensionLiftPos(700),
				new SleepAction(0.3),
				bot.setArmPos(bot.BACKWARDS_HIGH_CHAMBER_ARM_POS-300),
				new SleepAction(0.3),
				bot.setExtensionLiftPos(bot.extensionLift.minPos),
				new InstantAction(() -> {
					/* once the specimen is secured to the high chamber, forcefully release it and raise the arm */
					bot.intakeSmall.setPower(bot.INTAKE_SMALL_OUT_DIRECTION);
					bot.intakeLarge.setPower(bot.INTAKE_LARGE_OUT_DIRECTION);
				}),
				bot.setArmPos(bot.BACKWARDS_HIGH_CHAMBER_ARM_POS),
				new InstantAction(() -> {
					bot.intakeSmall.setPower(0);
					bot.intakeLarge.setPower(0);
				})
		);
	}

	Action ascend() {
		return bot.setArmPos(bot.AUTON_ASCENT_ARM_POS);
	}

	Action clutchPreload() {
		return new SequentialAction(
				new InstantAction(() -> {
					bot.intakeSmall.setPower(bot.INTAKE_SMALL_IN_DIRECTION);
					bot.intakeLarge.setPower(bot.INTAKE_LARGE_IN_DIRECTION);

				}),
				new SleepAction(1.5),
				new InstantAction(() -> {
					bot.intakeSmall.setPower(0);
					bot.intakeLarge.setPower(0);
				})
		);
	}

	protected void internalRun() {
		drive = new SparkFunOTOSDrive(hardwareMap, initialLeftPose);
		util = new MecanumUtil(drive);

		Action prepForPreloadHang = new ParallelAction(
				drive.actionBuilder(initialLeftPose)
						.setTangent(STANDARD_TANGENT)
						.lineToY(2.6*FieldConstants.BLOCK_LENGTH_IN)
						.strafeToSplineHeading(posForSpecimenDrop.position, posForSpecimenDrop.heading)
						.build(),
				prepareArmForHang()
		);

		Pose2d endOfNetting = new Pose2d(2.5*FieldConstants.BLOCK_LENGTH_IN, FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);

		Action netAndAscend = new SequentialAction(
				new ParallelAction(
						drive.actionBuilder(posForSpecimenDrop)
								.setTangent(Math.PI/2)
								.strafeToSplineHeading(backupFromChamber.position, backupFromChamber.heading)
								.build(),
						retractArm()
				),

				drive.actionBuilder(backupFromChamber)
						.setTangent(STANDARD_TANGENT)
						.lineToY(0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(2.05*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(STANDARD_TANGENT)
						.lineToY(2.6*FieldConstants.BLOCK_LENGTH_IN)
						.lineToY(0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(endOfNetting.position.x)
						.setTangent(STANDARD_TANGENT)
						.lineToY(2.6*FieldConstants.BLOCK_LENGTH_IN)
						.lineToY(endOfNetting.position.y)
						.build(),
				new ParallelAction(
						ascend(),
						drive.actionBuilder(endOfNetting)
								.strafeToSplineHeading(posForAscent.position, posForAscent.heading)
								.build()
				)
		);

		Action main = new SequentialAction(
				clutchPreload(),
				prepForPreloadHang,
				hangPreloadStationary(),
				netAndAscend
		);

		Actions.runBlocking(main);

		InteropFields.ARM_END_POS = bot.arm.getPosition();
		InteropFields.LIFT_END_POS = bot.extensionLift.getPosition();
	}
}
 	