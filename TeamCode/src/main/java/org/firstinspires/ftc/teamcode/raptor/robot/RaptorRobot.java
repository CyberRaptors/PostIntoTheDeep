package org.firstinspires.ftc.teamcode.raptor.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.teleop.IDriveableRobot;

public class RaptorRobot implements IDriveableRobot {
    public DcMotor leftBack;
    public DcMotor rightBack;
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor planeLauncher;
    public DcMotor testLift1;
    public DcMotor testLift2;
    public Servo hangServo;

    public void init(HardwareMap hardwareMap) {
		//DcMotors
		rightFront  = hardwareMap.get(DcMotor.class, "rightFront");
		leftFront = hardwareMap.get(DcMotor.class, "leftFront");
		rightBack = hardwareMap.get(DcMotor.class, "rightBack");
		leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        planeLauncher = hardwareMap.get(DcMotor.class, "lePlaneLauncher");
        testLift1 = hardwareMap.get(DcMotor.class, "testLift");
        testLift2 = hardwareMap.get(DcMotor.class, "testLiftDeux");
        hangServo = hardwareMap.get(Servo.class, "hangServo");
	}
}
