package lib8812.meepmeeptests;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import java.util.Optional;

public class MeepMeepLeftBasketCycle {
	static final double STANDARD_TANGENT = Math.PI / 2;

	final static Pose2d initialLeftPose = new Pose2d(1.5*FieldConstants.BLOCK_LENGTH_IN, (2.5*FieldConstants.BLOCK_LENGTH_IN+3.5), STANDARD_TANGENT);
	final static Pose2d posForAscent = new Pose2d(FieldConstants.BLOCK_LENGTH_IN+2,0.45*FieldConstants.BLOCK_LENGTH_IN, 0);
	final static Pose2d posForHighBasketBackDrop = new Pose2d(2.3*FieldConstants.BLOCK_LENGTH_IN, 2.1*FieldConstants.BLOCK_LENGTH_IN, 5*Math.PI/4);

	public static Action run(DriveShim drive) {
		ActionableRaptorRobotStub bot = new ActionableRaptorRobotStub();
		drive.setPoseEstimate(initialLeftPose);

		Action dropFirstAndPickupSecond = new SequentialAction(
				drive.actionBuilder(initialLeftPose)
						.setTangent(4*Math.PI/5)
						.lineToYSplineHeading(posForHighBasketBackDrop.position.y, posForHighBasketBackDrop.heading)
						.build(),
				bot.prepareArmForBackDrop(),
				bot.spitSample(),
				bot.standardFrog()
		);

		Action dropSecondAndPickupThird = new SequentialAction(

		);

		Action main = new SequentialAction(
				dropFirstAndPickupSecond
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