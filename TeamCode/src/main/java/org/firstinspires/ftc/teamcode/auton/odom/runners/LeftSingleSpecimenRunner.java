package org.firstinspires.ftc.teamcode.auton.odom.runners;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.auton.MecanumUtil;
import lib8812.common.robot.IMecanumRobot;
import lib8812.common.rr.SparkFunOTOSDrive;
import lib8812.common.telemetrymap.FieldConstants;
import lib8812.common.teleop.ITeleOpRunner;

public class LeftSingleSpecimenRunner extends ITeleOpRunner { // this can impl ITeleOpRunner because no object detection is needed
	static final double STANDARD_TANGENT = 3 * Math.PI / 2;

	final static Pose2d initialBlueLeftPose = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, (2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), STANDARD_TANGENT);
	final static Pose2d posForSpecimenDrop = new Pose2d(7, FieldConstants.BLOCK_LENGTH_IN+11, 3*Math.PI/2);
	final static Pose2d backupFromChamber = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, 1.5*FieldConstants.BLOCK_LENGTH_IN, Math.PI);
	final static Pose2d posForAscent = new Pose2d(FieldConstants.BLOCK_LENGTH_IN+5,0, Math.PI);

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
				bot.setArmPos(bot.BACKWARDS_HIGH_CHAMBER_ARM_POS-75),
				new SleepAction(0.5),
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
				new SleepAction(0.5),
				new InstantAction(() -> {
					bot.intakeSmall.setPower(0);
					bot.intakeLarge.setPower(0);
				})
		);
	}

	protected void internalRun() {
		drive = new SparkFunOTOSDrive(hardwareMap, initialBlueLeftPose);
		util = new MecanumUtil(drive);

		Action prepForPreloadHang = new ParallelAction(
				drive.actionBuilder(initialBlueLeftPose)
						.setTangent(STANDARD_TANGENT)
						.lineToY(2.5*FieldConstants.BLOCK_LENGTH_IN)
						.splineToSplineHeading(posForSpecimenDrop, STANDARD_TANGENT)
						.build(),
				prepareArmForHang()
		);

		Action proceedToAscent = new SequentialAction(
				new ParallelAction(
						drive.actionBuilder(posForSpecimenDrop)
								.setTangent(Math.PI/2)
								.splineToSplineHeading(backupFromChamber, 0)
								.build(),
						retractArm()
				),
				drive.actionBuilder(backupFromChamber)
						.setTangent(STANDARD_TANGENT)
						.lineToY(0)
						.setTangent(0)
						.lineToX(1.3*FieldConstants.BLOCK_LENGTH_IN)
						.build()

		);

		Action main = new SequentialAction(
				clutchPreload(),
				prepForPreloadHang,
				hangPreloadStationary(),
				proceedToAscent,
				ascend()
		);

		Actions.runBlocking(main);
	}
}
 	