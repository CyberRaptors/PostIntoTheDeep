package lib8812.meepmeeptests.right;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.ActionableRaptorRobotStub;
import lib8812.meepmeeptests.FieldConstants;

public class MeepMeepRightSpecimenCycle {
	static final double STANDARD_TANGENT = 3 * Math.PI / 2;

	final static Pose2d initialRightPose = new Pose2d(0.5* FieldConstants.BLOCK_LENGTH_IN, -(2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), STANDARD_TANGENT);
	final static Pose2d posForSpecimenDrop = new Pose2d(0.457*FieldConstants.BLOCK_LENGTH_IN, -(1.5*FieldConstants.BLOCK_LENGTH_IN-5), STANDARD_TANGENT);
	final static Pose2d backupFromChamber = new Pose2d(1.35*FieldConstants.BLOCK_LENGTH_IN, -1.5*FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);
	final static Pose2d specimenPickupPos = new Pose2d(2*FieldConstants.BLOCK_LENGTH_IN, -(2*FieldConstants.BLOCK_LENGTH_IN-2), STANDARD_TANGENT);
	final static Pose2d startPushingFirstSampleFrom = new Pose2d(2.*FieldConstants.BLOCK_LENGTH_IN+2.5, -0.4*FieldConstants.BLOCK_LENGTH_IN, 3*Math.PI/2);
	final static Pose2d startPushingSecondSampleFrom = new Pose2d(2.7*FieldConstants.BLOCK_LENGTH_IN, -0.4*FieldConstants.BLOCK_LENGTH_IN, 3*Math.PI/2);
	final static double pushSamplesUntil = -2.3*FieldConstants.BLOCK_LENGTH_IN;

	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(initialRightPose);

		ActionableRaptorRobotStub bot = new ActionableRaptorRobotStub();

		Action prepForPreloadHang = new ParallelAction(
				drive.actionBuilder(initialRightPose)
						.setTangent(STANDARD_TANGENT)
						.strafeToSplineHeading(posForSpecimenDrop.position, posForSpecimenDrop.heading)
						.build(),
				bot.prepareArmForSpecimenHang()
		);

		Action netFirstAndSecond = new SequentialAction(
				new ParallelAction(
						bot.retractArm(),
						drive.actionBuilder(posForSpecimenDrop)
//								.strafeToSplineHeading(backupFromChamber.position, backupFromChamber.heading)
//								.strafeTo(new Vector2d(1.7*FieldConstants.BLOCK_LENGTH_IN, -0.4*FieldConstants.BLOCK_LENGTH_IN))
//								.lineToY(-2.2*FieldConstants.BLOCK_LENGTH_IN)
								.splineToSplineHeading(startPushingFirstSampleFrom, 0)
								.setTangent(STANDARD_TANGENT)
								.lineToY(pushSamplesUntil)
								.lineToY(-0.4*FieldConstants.BLOCK_LENGTH_IN)
								.setTangent(0)
								.lineToX(2.5*FieldConstants.BLOCK_LENGTH_IN)
								.setTangent(STANDARD_TANGENT)
								.lineToY(pushSamplesUntil)
								.strafeToSplineHeading(specimenPickupPos.position, specimenPickupPos.heading)
								.build()
				)
		);

		Action netThird = drive.actionBuilder(posForSpecimenDrop)
				.lineToY(-1.7*FieldConstants.BLOCK_LENGTH_IN)
				.splineToSplineHeading(startPushingFirstSampleFrom, startPushingFirstSampleFrom.heading)
				.setTangent(STANDARD_TANGENT)
				.lineToY(pushSamplesUntil)
				.strafeToSplineHeading(specimenPickupPos.position, specimenPickupPos.heading)
				.build();

		Action pickupSpecimen = new SequentialAction(
				bot.setMaxArmPos(),
				bot.standardFrog()
		);

		Action prepForHang = new ParallelAction(
				bot.prepareArmForSpecimenHang(),
				drive.actionBuilder(specimenPickupPos)
						.strafeToSplineHeading(posForSpecimenDrop.position, posForSpecimenDrop.heading)
						.build()
		);

		Action returnToOZ = new ParallelAction(
				bot.retractArm(),
				drive.actionBuilder(posForSpecimenDrop)
						.strafeToSplineHeading(specimenPickupPos.position, posForSpecimenDrop.heading)
						.build()
		);

		Action pickupAndHang = new SequentialAction(
				pickupSpecimen,
				prepForHang,
				bot.hangPreloadStationary()
		);

		Action park = drive.actionBuilder(specimenPickupPos)
				.setTangent(STANDARD_TANGENT)
				.lineToY(-2.6*FieldConstants.BLOCK_LENGTH_IN)
				.build();

		Action main = new SequentialAction(
				bot.clutchPreload(),
				prepForPreloadHang,
				bot.hangPreloadStationary(),
				netFirstAndSecond,
				pickupAndHang,
				returnToOZ,
//				netThird, // -> replace this with a returnToOZ (above) if not going for third
//				pickupAndHang,
//				returnToOZ,
				pickupAndHang,
				returnToOZ,
//				pickupAndHang, // could do a last one, but need teammate to allow use to use their specimen for this
//				returnToOZ,
				park
		);

		return main;
	}
}