package lib8812.common.robot;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.DcMotor;

import lib8812.common.robot.hardwarewrappers.LimelightManager;

public class MecanumLimelightAlgorithms { // NOTE: THEN USE TY IN THE SAME WAY TO DETERMINE IF ROBOT IS CORRECT DISTANCE AWAY
	final LimelightManager limelight;
	final IMecanumRobot bot;
	final double targetSize;
	double centeringKp = 0.001;
	double acceptableError = 10;
	double acceptableSizeError = 5;

	public MecanumLimelightAlgorithms(IMecanumRobot bot, LimelightManager limelightMgr, double targetSize) {
		  limelight = limelightMgr;
		  this.targetSize = targetSize;
		  this.bot = bot;
	}

	public void setCenteringKp(double kP) {
		centeringKp = kP;
	}

	public void setAcceptableError(double err) {
		acceptableError = err;
	}

	public void setAcceptableSizeError(double err) {
		acceptableSizeError = err;
	}


	public Action faceTarget() { // TODO: add timeout to all
		return new Action() {
			final DcMotor.ZeroPowerBehavior leftFrontBehavior = bot.leftFront.getZeroPowerBehavior();
			final DcMotor.ZeroPowerBehavior leftBackBehavior = bot.leftBack.getZeroPowerBehavior();
			final DcMotor.ZeroPowerBehavior rightFrontBehavior = bot.rightFront.getZeroPowerBehavior();
			final DcMotor.ZeroPowerBehavior rightBackBehavior = bot.rightBack.getZeroPowerBehavior();
			boolean started;


			@Override
			public boolean run(@NonNull TelemetryPacket telemetryPacket) {
				if (!started) {
					bot.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
					bot.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
					bot.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
					bot.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

					started = true;
				}

				LLResult res = limelight.getLatestResultEnsure();
				double err = res.getTx();

				if (Math.abs(err) < acceptableError) {
					bot.leftFront.setPower(0);
					bot.leftBack.setPower(0);
					bot.rightFront.setPower(0);
					bot.rightBack.setPower(0);

					bot.leftFront.setZeroPowerBehavior(leftFrontBehavior);
					bot.leftBack.setZeroPowerBehavior(leftBackBehavior);
					bot.rightFront.setZeroPowerBehavior(rightFrontBehavior);
					bot.rightBack.setZeroPowerBehavior(rightBackBehavior);

					return false;
				}

				double leftFeedback = centeringKp*err;
				double rightFeedback = -leftFeedback;

				bot.leftFront.setPower(leftFeedback);
				bot.leftBack.setPower(leftFeedback);

				bot.rightFront.setPower(rightFeedback);
				bot.rightBack.setPower(rightFeedback);

				return true;
			}
		};
	}

	public Action alignDistanceFromTarget() {
		return new Action() {
			final DcMotor.ZeroPowerBehavior leftFrontBehavior = bot.leftFront.getZeroPowerBehavior();
			final DcMotor.ZeroPowerBehavior leftBackBehavior = bot.leftBack.getZeroPowerBehavior();
			final DcMotor.ZeroPowerBehavior rightFrontBehavior = bot.rightFront.getZeroPowerBehavior();
			final DcMotor.ZeroPowerBehavior rightBackBehavior = bot.rightBack.getZeroPowerBehavior();
			boolean started;


			@Override
			public boolean run(@NonNull TelemetryPacket telemetryPacket) {
				if (!started) {
					bot.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
					bot.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
					bot.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
					bot.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

					started = true;
				}

				LLResult res = limelight.getLatestResultEnsure();
				double err = targetSize-res.getTa();

				if (Math.abs(err) < acceptableSizeError) {
					bot.leftFront.setPower(0);
					bot.leftBack.setPower(0);
					bot.rightFront.setPower(0);
					bot.rightBack.setPower(0);

					bot.leftFront.setZeroPowerBehavior(leftFrontBehavior);
					bot.leftBack.setZeroPowerBehavior(leftBackBehavior);
					bot.rightFront.setZeroPowerBehavior(rightFrontBehavior);
					bot.rightBack.setZeroPowerBehavior(rightBackBehavior);

					return false;
				}

				double feedback = centeringKp*err;

				bot.leftFront.setPower(feedback);
				bot.leftBack.setPower(feedback);
				bot.rightFront.setPower(feedback);
				bot.rightBack.setPower(feedback);

				return true;
			}
		};
	}

	public Action alignAndFaceTarget() {
		return new SequentialAction(faceTarget(), alignDistanceFromTarget());
	}
}
