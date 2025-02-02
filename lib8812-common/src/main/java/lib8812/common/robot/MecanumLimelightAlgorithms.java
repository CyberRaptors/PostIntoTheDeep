package lib8812.common.robot;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.DcMotor;

import lib8812.common.robot.hardwarewrappers.LimelightManager;
import lib8812.common.rr.MecanumDrive;

public class MecanumLimelightAlgorithms { // NOTE: THEN USE TY IN THE SAME WAY TO DETERMINE IF ROBOT IS CORRECT DISTANCE AWAY
	final LimelightManager limelight;
	final MecanumDrive drive;
	final double targetSize;
	double centeringKp = 0.001;
	double acceptableError = 10;
	double acceptableSizeError = 5;

	public MecanumLimelightAlgorithms(MecanumDrive drive, LimelightManager limelightMgr, double targetSize) {
		  limelight = limelightMgr;
		  this.targetSize = targetSize;
		  this.drive = drive;
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
			final DcMotor.ZeroPowerBehavior leftFrontBehavior = drive.leftFront.getZeroPowerBehavior();
			final DcMotor.ZeroPowerBehavior leftBackBehavior = drive.leftBack.getZeroPowerBehavior();
			final DcMotor.ZeroPowerBehavior rightFrontBehavior = drive.rightFront.getZeroPowerBehavior();
			final DcMotor.ZeroPowerBehavior rightBackBehavior = drive.rightBack.getZeroPowerBehavior();
			boolean started;


			@Override
			public boolean run(@NonNull TelemetryPacket telemetryPacket) {
				if (!started) {
					drive.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
					drive.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
					drive.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
					drive.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

					started = true;
				}

				LLResult res = limelight.getLatestResultEnsure();
				double err = res.getTx();

				if (Math.abs(err) < acceptableError) {
					drive.leftFront.setPower(0);
					drive.leftBack.setPower(0);
					drive.rightFront.setPower(0);
					drive.rightBack.setPower(0);

					drive.leftFront.setZeroPowerBehavior(leftFrontBehavior);
					drive.leftBack.setZeroPowerBehavior(leftBackBehavior);
					drive.rightFront.setZeroPowerBehavior(rightFrontBehavior);
					drive.rightBack.setZeroPowerBehavior(rightBackBehavior);

					return false;
				}

				double leftFeedback = centeringKp*err;
				double rightFeedback = -leftFeedback;

				drive.leftFront.setPower(leftFeedback);
				drive.leftBack.setPower(leftFeedback);

				drive.rightFront.setPower(rightFeedback);
				drive.rightBack.setPower(rightFeedback);

				return true;
			}
		};
	}

	public Action alignDistanceFromTarget() {
		return new Action() {
			final DcMotor.ZeroPowerBehavior leftFrontBehavior = drive.leftFront.getZeroPowerBehavior();
			final DcMotor.ZeroPowerBehavior leftBackBehavior = drive.leftBack.getZeroPowerBehavior();
			final DcMotor.ZeroPowerBehavior rightFrontBehavior = drive.rightFront.getZeroPowerBehavior();
			final DcMotor.ZeroPowerBehavior rightBackBehavior = drive.rightBack.getZeroPowerBehavior();
			boolean started;


			@Override
			public boolean run(@NonNull TelemetryPacket telemetryPacket) {
				if (!started) {
					drive.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
					drive.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
					drive.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
					drive.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

					started = true;
				}

				LLResult res = limelight.getLatestResultEnsure();
				double err = targetSize-res.getTa();

				if (Math.abs(err) < acceptableSizeError) {
					drive.leftFront.setPower(0);
					drive.leftBack.setPower(0);
					drive.rightFront.setPower(0);
					drive.rightBack.setPower(0);

					drive.leftFront.setZeroPowerBehavior(leftFrontBehavior);
					drive.leftBack.setZeroPowerBehavior(leftBackBehavior);
					drive.rightFront.setZeroPowerBehavior(rightFrontBehavior);
					drive.rightBack.setZeroPowerBehavior(rightBackBehavior);

					return false;
				}

				double feedback = centeringKp*err;

				drive.leftFront.setPower(feedback);
				drive.leftBack.setPower(feedback);
				drive.rightFront.setPower(feedback);
				drive.rightBack.setPower(feedback);

				return true;
			}
		};
	}

	public Action alignAndFaceTarget() {
		return new SequentialAction(faceTarget(), alignDistanceFromTarget());
	}
}
