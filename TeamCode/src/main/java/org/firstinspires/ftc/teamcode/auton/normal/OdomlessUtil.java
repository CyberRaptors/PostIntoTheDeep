package org.firstinspires.ftc.teamcode.auton.normal;

import com.qualcomm.robotcore.hardware.DcMotor;

import lib8812.common.robot.IMecanumRobot;

public class OdomlessUtil {
	public interface SleepFunction {
		void run(long ms);
	}

	final SleepFunction sleep;
	final IMecanumRobot bot;


	public OdomlessUtil(IMecanumRobot robot, SleepFunction sleepFunc) {
		sleep = sleepFunc;
		bot = robot;
	}

	public void init() {
		bot.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		bot.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		bot.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		bot.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
	}

	public void moveSync(long ms, double power) {
		bot.leftFront.setPower(power);
		bot.rightFront.setPower(power);
		bot.leftBack.setPower(power);
		bot.rightBack.setPower(power);

		sleep.run(ms);

		bot.leftFront.setPower(0);
		bot.rightFront.setPower(0);
		bot.leftBack.setPower(0);
		bot.rightBack.setPower(0);
	}

	public void strafeSync(long ms, double power) { // positive power strafes right
		bot.leftFront.setPower(power);
		bot.rightFront.setPower(-power);
		bot.leftBack.setPower(-power);
		bot.rightBack.setPower(power);

		sleep.run(ms);

		bot.leftFront.setPower(0);
		bot.rightFront.setPower(0);
		bot.leftBack.setPower(0);
		bot.rightBack.setPower(0);
	}

	public void turn(long ms, double power) { // positive power turns clockwise

	}
}
