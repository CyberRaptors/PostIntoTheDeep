package org.firstinspires.ftc.teamcode.auton.odom.runners;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.rr.SparkFunOTOSDrive;
import lib8812.common.telemetrymap.FieldConstants;
import lib8812.common.teleop.ITeleOpRunner;

public class BlueLeftSingleSpecimenRunner extends ITeleOpRunner { // this can impl ITeleOpRunner because no object detection is needed
	final static Pose2d initialPose = new Pose2d(-(2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), 1.5*FieldConstants.BLOCK_LENGTH_IN, Math.PI);

	final static Vector2d posForSpecimenDrop = new Vector2d(-(FieldConstants.BLOCK_LENGTH_IN+11), 7);

	final RaptorRobot bot = new RaptorRobot();
	SparkFunOTOSDrive drive;

	protected IMecanumRobot getBot() { return bot; }


	protected void internalRun() {
		drive = new SparkFunOTOSDrive(hardwareMap, initialPose);

		bot.intakeSmall.setPower(bot.INTAKE_SMALL_IN_DIRECTION);
		bot.intakeLarge.setPower(bot.INTAKE_LARGE_IN_DIRECTION);

		sleep(100);

		bot.intakeSmall.setPower(0);
		bot.intakeLarge.setPower(0);


		Action hangPreload = drive.actionBuilder(initialPose)
				.setTangent(0)
				.lineToX(-2.5*FieldConstants.BLOCK_LENGTH_IN)
				.splineTo(posForSpecimenDrop, 0)
				.afterDisp(3, () -> bot.arm.setPosition(bot.BACKWARDS_HIGH_CHAMBER_ARM_POS))
				.build();

		Actions.runBlocking(hangPreload);
	}
}
 	