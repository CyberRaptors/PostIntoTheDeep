package lib8812.meepmeeptests;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

public class MeepMeepLeft {
	static final double STANDARD_TANGENT = Math.PI / 2;

	final static Pose2d initialLeftPose = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, (2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), STANDARD_TANGENT);
	final static Pose2d posForSpecimenDrop = new Pose2d(0.457*FieldConstants.BLOCK_LENGTH_IN, 1.5*FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);
	final static Pose2d backupFromChamber = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, 1.5*FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);
	final static Pose2d netZone = new Pose2d(2.4*FieldConstants.BLOCK_LENGTH_IN, 2.4*FieldConstants.BLOCK_LENGTH_IN, 5*Math.PI/4);
	final static Pose2d posForAscent = new Pose2d(FieldConstants.BLOCK_LENGTH_IN+2,0.43*FieldConstants.BLOCK_LENGTH_IN, 0);


	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(initialLeftPose);

		Action prepForPreloadHang = new ParallelAction(
				drive.actionBuilder(initialLeftPose)
						.setTangent(STANDARD_TANGENT)
						.lineToY(2.6*FieldConstants.BLOCK_LENGTH_IN)
						.splineToSplineHeading(posForSpecimenDrop, 0)
						.build(),
				prepareArmForHang()
		);

		Pose2d endOfNetting = new Pose2d(2.5*FieldConstants.BLOCK_LENGTH_IN, FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);

		Action netAndAscend = new SequentialAction(
				new ParallelAction(
						drive.actionBuilder(posForSpecimenDrop)
								.setTangent(Math.PI/2)
								.splineToSplineHeading(backupFromChamber, 0)
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
								.splineToSplineHeading(posForAscent, Math.PI)
								.build()
				)

		);

		Action main = new SequentialAction(
				clutchPreload(),
				prepForPreloadHang,
				hangPreloadStationary(),
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