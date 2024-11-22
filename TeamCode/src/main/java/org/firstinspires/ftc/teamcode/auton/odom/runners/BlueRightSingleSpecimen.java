package org.firstinspires.ftc.teamcode.auton.odom.runners;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.robot.MergedRaptorRobot;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.rr.MecanumDrive;
import lib8812.common.teleop.ITeleOpRunner;

public class BlueRightSingleSpecimen extends ITeleOpRunner { // this can impl ITeleOpRunner because no object detection is needed
	final static Pose2d initialPose = new Pose2d(0, 0, Math.PI / 2);

	final static Vector2d posForSpecimenDrop = new Vector2d(10, 14);

	final MergedRaptorRobot bot = new MergedRaptorRobot();
	final MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

	protected IMecanumRobot getBot() { return bot; }


	protected void internalRun() {

		Action hangPreload = drive.actionBuilder(initialPose)
				.splineTo(posForSpecimenDrop, initialPose.heading)
				.stopAndAdd(() -> bot.arm.setPosition(bot.BACKWARDS_HIGH_CHAMBER_ARM_POS))
				.build();

	}
}
 	