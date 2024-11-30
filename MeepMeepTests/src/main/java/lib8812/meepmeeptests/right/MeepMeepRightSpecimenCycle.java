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
	final static Pose2d posForSpecimenDrop = new Pose2d(0.457*FieldConstants.BLOCK_LENGTH_IN, -(1.5*FieldConstants.BLOCK_LENGTH_IN), STANDARD_TANGENT);
	final static Pose2d backupFromChamber = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, -1.5*FieldConstants.BLOCK_LENGTH_IN, STANDARD_TANGENT);
	final static Pose2d specimenPickupPos = new Pose2d(2* FieldConstants.BLOCK_LENGTH_IN, -(2*FieldConstants.BLOCK_LENGTH_IN-5), STANDARD_TANGENT);

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

		Action netAll = new SequentialAction(
				new ParallelAction(
						drive.actionBuilder(posForSpecimenDrop)
								.setTangent(0)
								.lineToX(backupFromChamber.position.x)
								.build(),
						bot.retractArm()
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
						.lineToY(-0.4*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(0)
						.lineToX(2.7*FieldConstants.BLOCK_LENGTH_IN)
						.setTangent(STANDARD_TANGENT)
						.lineToY(-2.6*FieldConstants.BLOCK_LENGTH_IN)
						.strafeToSplineHeading(specimenPickupPos.position, specimenPickupPos.heading)
						.build()
		);

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

		Action hangAndReturn = new SequentialAction(
				pickupSpecimen,
				prepForHang,
				bot.hangPreloadStationary(),
				returnToOZ
		);

		Action park = drive.actionBuilder(specimenPickupPos)
				.setTangent(STANDARD_TANGENT)
				.lineToY(-2.6*FieldConstants.BLOCK_LENGTH_IN)
				.build();

		Action main = new SequentialAction(
				bot.clutchPreload(),
				prepForPreloadHang,
				bot.hangPreloadStationary(),
				netAll,
				hangAndReturn,
				hangAndReturn,
				hangAndReturn,
				hangAndReturn, // could do a fifth, but need teammate to allow use to use their specimen for this
				park
		);

		return main;
	}
}