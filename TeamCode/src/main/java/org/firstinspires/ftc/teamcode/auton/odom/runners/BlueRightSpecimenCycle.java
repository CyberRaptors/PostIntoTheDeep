package org.firstinspires.ftc.teamcode.auton.odom.runners;

import org.firstinspires.ftc.teamcode.robot.MergedRaptorRobot;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.robot.poses.SimpleDegreePose;
import lib8812.common.rr.MecanumDrive;
import lib8812.common.teleop.ITeleOpRunner;

public class BlueRightSpecimenCycle extends ITeleOpRunner { // this can impl ITeleOpRunner because no object detection is needed
	final static SimpleDegreePose initialPose = new SimpleDegreePose(0, 0, 0);

	final MergedRaptorRobot robot = new MergedRaptorRobot();
	final MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose.toRoadRunner());

	protected IMecanumRobot getBot() { return robot; }


	protected void internalRun() {

	}
}
 	