package org.firstinspires.ftc.teamcode.auton.odom.runners.right;

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

public class RightSingleSpecimenRunner extends ITeleOpRunner { // this can impl ITeleOpRunner because no object detection is needed
	static final double STANDARD_TANGENT = 3 * Math.PI / 2;

	final static Pose2d initialRightPose = new Pose2d(0.5* FieldConstants.BLOCK_LENGTH_IN, -(2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), STANDARD_TANGENT);
	final static Pose2d posForSpecimenDrop = new Pose2d(0.457*FieldConstants.BLOCK_LENGTH_IN, -(1.5*FieldConstants.BLOCK_LENGTH_IN), STANDARD_TANGENT);
	final static Pose2d backupFromChamber = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, -1.5*FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);

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
				bot.setExtensionLiftPos(400),
				bot.setArmPos(bot.BACKWARDS_HIGH_CHAMBER_ARM_POS-150),
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
		drive = new SparkFunOTOSDrive(hardwareMap, initialRightPose);
		util = new MecanumUtil(drive);

		Action prepForPreloadHang = new ParallelAction(
				drive.actionBuilder(initialRightPose)
						.setTangent(STANDARD_TANGENT)
						.strafeToSplineHeading(posForSpecimenDrop.position, posForSpecimenDrop.heading)
						.build(),
				prepareArmForHang()
		);

		Action netAndPark = new SequentialAction(
				new ParallelAction(
						drive.actionBuilder(posForSpecimenDrop)
								.setTangent(0)
								.lineToX(backupFromChamber.position.x)
								.build(),
						retractArm()
				),

				drive.actionBuilder(backupFromChamber)
						.setTangent(STANDARD_TANGENT)
						.lineToY(-0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(2.05*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(STANDARD_TANGENT)
						.lineToY(-2.6*FieldConstants.BLOCK_LENGTH_IN)
						.lineToY(-0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(2.5*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(STANDARD_TANGENT)
						.lineToY(-2.6*FieldConstants.BLOCK_LENGTH_IN)
						.build()

		);

		Action main = new SequentialAction(
				clutchPreload(),
				prepForPreloadHang,
				hangPreloadStationary(),
				netAndPark
		);



		Actions.runBlocking(main);

		InteropFields.ARM_END_POS = bot.arm.getPosition();
		InteropFields.LIFT_END_POS = bot.extensionLift.getPosition();
	}
}
 	