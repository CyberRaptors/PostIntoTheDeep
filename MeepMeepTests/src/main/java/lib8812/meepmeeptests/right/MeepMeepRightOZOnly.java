package lib8812.meepmeeptests.right;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.FieldConstants;

public class MeepMeepRightOZOnly {
	static final double STANDARD_TANGENT = 3 * Math.PI / 2;

	final static Pose2d initialRightPose = new Pose2d(0.5* FieldConstants.BLOCK_LENGTH_IN, -(2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), STANDARD_TANGENT);
	final static Pose2d posForSpecimenDrop = new Pose2d(0.457*FieldConstants.BLOCK_LENGTH_IN, -(FieldConstants.BLOCK_LENGTH_IN+10), STANDARD_TANGENT);
	final static Pose2d backupFromChamber = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, -1.5*FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);


	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(initialRightPose);

		Action netAndAscend = new SequentialAction(
				new ParallelAction(
						drive.actionBuilder(initialRightPose)
								.setTangent(Math.PI/2)
								.splineToSplineHeading(backupFromChamber, Math.PI/2)
								.build()
				),

				drive.actionBuilder(backupFromChamber)
						.setTangent(STANDARD_TANGENT)
						.lineToY(-0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(2*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(STANDARD_TANGENT)
						.lineToY(-2.5*FieldConstants.BLOCK_LENGTH_IN)
						.lineToY(-0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(2.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(STANDARD_TANGENT)
						.lineToY(-2.5*FieldConstants.BLOCK_LENGTH_IN)
						.build()

		);

		Action main = new SequentialAction(
				netAndAscend
		);

		return main;
	}

	private static Action retractArm() {
		return new SleepAction(1);
	}

	private static Action clutchPreload() {
		return new SleepAction(0.5);
	}

	private static Action hangPreloadStationary() {
		return new SleepAction(5);
	}

	private static Action ascend() {
		return new SleepAction(2);
	}

	private static Action prepareArmForHang() {
		return new SleepAction(2);
	}
}