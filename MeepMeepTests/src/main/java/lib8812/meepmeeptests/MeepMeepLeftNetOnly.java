package lib8812.meepmeeptests;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

public class MeepMeepLeftNetOnly {
	static final double STANDARD_TANGENT = Math.PI / 2;

	final static Pose2d initialBlueLeftPose = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, (2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), STANDARD_TANGENT);
	final static Pose2d backupFromChamber = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, 1.5*FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);
	final static Pose2d netZone = new Pose2d(2.4*FieldConstants.BLOCK_LENGTH_IN, 2.4*FieldConstants.BLOCK_LENGTH_IN, 5*Math.PI/4);
	final static Pose2d posForAscent = new Pose2d(FieldConstants.BLOCK_LENGTH_IN+2,0.45*FieldConstants.BLOCK_LENGTH_IN, 0);


	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(initialBlueLeftPose);

		Pose2d endOfNetting = new Pose2d(2.4*FieldConstants.BLOCK_LENGTH_IN, FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);

		Action netAndAscend = new SequentialAction(
				drive.actionBuilder(initialBlueLeftPose)
						.setTangent(STANDARD_TANGENT)
						.lineToY(0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(2*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(STANDARD_TANGENT)
						.lineToY(2.5*FieldConstants.BLOCK_LENGTH_IN)
						.lineToY(0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(endOfNetting.position.x)
						.setTangent(STANDARD_TANGENT)
						.lineToY(2.5*FieldConstants.BLOCK_LENGTH_IN)
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