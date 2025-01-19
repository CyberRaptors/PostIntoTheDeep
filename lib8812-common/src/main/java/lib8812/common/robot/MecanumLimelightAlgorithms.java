package lib8812.common.robot;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.DcMotor;

import lib8812.common.robot.hardwarewrappers.LimelightManager;

public class MecanumLimelightAlgorithms { // NOTE: THEN USE TY IN THE SAME WAY TO DETERMINE IF ROBOT IS CORRECT DISTANCE AWAY
	final LimelightManager limelight;
	final IMecanumRobot bot;
	double centeringKp = 0.001;
	double acceptableError = 10;

	public MecanumLimelightAlgorithms(IMecanumRobot bot, LimelightManager limelightMgr) {
		  limelight = limelightMgr;
		  this.bot = bot;
	}

	public void setCenteringKp(double kP) {
		centeringKp = kP;
	}

	public void setAcceptableError(double err) {
		acceptableError = err;
	}

	public void faceTarget() {
		DcMotor.ZeroPowerBehavior leftFrontBehavior = bot.leftFront.getZeroPowerBehavior();
		DcMotor.ZeroPowerBehavior leftBackBehavior = bot.leftBack.getZeroPowerBehavior();
		DcMotor.ZeroPowerBehavior rightFrontBehavior = bot.rightFront.getZeroPowerBehavior();
		DcMotor.ZeroPowerBehavior rightBackBehavior = bot.rightBack.getZeroPowerBehavior();

		bot.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		bot.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		bot.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		bot.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		double err = Double.MAX_VALUE;

		while (Math.abs(err) > acceptableError) {
			LLResult res = limelight.getLatestResultEnsure();

			err = res.getTx();

			double leftFeedback = centeringKp*err;
			double rightFeedback = -leftFeedback;

			bot.leftFront.setPower(leftFeedback);
			bot.leftBack.setPower(leftFeedback);

			bot.rightFront.setPower(rightFeedback);
			bot.rightBack.setPower(rightFeedback);

		}

		bot.leftFront.setPower(0);
		bot.leftBack.setPower(0);
		bot.rightFront.setPower(0);
		bot.rightBack.setPower(0);

		bot.leftFront.setZeroPowerBehavior(leftFrontBehavior);
		bot.leftBack.setZeroPowerBehavior(leftBackBehavior);
		bot.rightFront.setZeroPowerBehavior(rightFrontBehavior);
		bot.rightBack.setZeroPowerBehavior(rightBackBehavior);
	}
}
