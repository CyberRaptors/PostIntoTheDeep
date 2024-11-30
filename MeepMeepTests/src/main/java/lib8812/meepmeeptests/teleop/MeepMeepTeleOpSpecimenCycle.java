package lib8812.meepmeeptests.teleop;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.FieldConstants;

public class MeepMeepTeleOpSpecimenCycle {
	static final double STANDARD_TANGENT = 3 * Math.PI / 2;

	final static Pose2d initialSpecimenPickupPose = new Pose2d(2* FieldConstants.BLOCK_LENGTH_IN, -(2*FieldConstants.BLOCK_LENGTH_IN-5), STANDARD_TANGENT);
	final static Pose2d posForSpecimenDrop = new Pose2d(0.2*FieldConstants.BLOCK_LENGTH_IN, -(1.5*FieldConstants.BLOCK_LENGTH_IN), STANDARD_TANGENT);

	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(initialSpecimenPickupPose);


		Action prepForHang = new ParallelAction(
				drive.actionBuilder(initialSpecimenPickupPose)
						.strafeToSplineHeading(posForSpecimenDrop.position, posForSpecimenDrop.heading)
						.build(),
				prepareArmForHang()
		);

		Action returnToOZ = new ParallelAction(
				retractArm(),
				drive.actionBuilder(posForSpecimenDrop)
						.strafeToSplineHeading(initialSpecimenPickupPose.position, posForSpecimenDrop.heading)
						.build()
		);

		Action main = new SequentialAction(
				prepForHang,
				hangSpecimenStationary(),
				returnToOZ
		);


		return main;
	}

	private static Action retractArm() {
		return new SleepAction(1);
	}

	private static Action clutchPreload() {
		return new SleepAction(0.5);
	}

	private static Action hangSpecimenStationary() {
		return new SleepAction(5);
	}

	private static Action ascend() {
		return new SleepAction(2);
	}

	private static Action prepareArmForHang() {
		return new SleepAction(2);
	}
}