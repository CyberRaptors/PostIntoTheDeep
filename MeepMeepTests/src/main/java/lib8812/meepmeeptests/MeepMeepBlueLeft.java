package lib8812.meepmeeptests;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

public class MeepMeepBlueLeft {
	static final double STANDARD_TANGENT = 3 * Math.PI / 2;

	final static Pose2d initialBlueLeftPose = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, (2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), STANDARD_TANGENT);
	final static Pose2d posForSpecimenDrop = new Pose2d(7, FieldConstants.BLOCK_LENGTH_IN+11, 3*Math.PI/2);
	final static Pose2d backupFromChamber = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, 1.5*FieldConstants.BLOCK_LENGTH_IN, Math.PI);
	final static Pose2d posForAscent = new Pose2d(FieldConstants.BLOCK_LENGTH_IN+5,0, Math.PI);


	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(initialBlueLeftPose);

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