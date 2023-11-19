package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.VirtualMotor;
import lib8812.common.robot.VirtualServo;
import lib8812.common.teleop.IDriveableRobot;

public class RaptorRobot extends IDriveableRobot {
    public final double CLAW_OPEN = 0.4;
    public final double CLAW_CLOSED = 0;
    public final double PLANE_SHOT = 0.5;
    public final double PLANE_READY = 0.7;

    public DcMotor leftBack;
    public DcMotor rightBack;
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor testLift1;
    public DcMotor testLift2;
    public Servo claw;
    public CRServo clawRotate1;
    public CRServo clawRotate2;

    // mechanical devices not added yet
    public DcMotor arm;
    public Servo planeShooter;
    public VirtualMotor spinningIntake;

    public void init(HardwareMap hardwareMap) {
		rightFront  = loadDevice(hardwareMap, DcMotor.class, "rightFront");
		leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
		rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
		leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");

        testLift1 = loadDevice(hardwareMap, DcMotor.class, "testLift");
        testLift2 = loadDevice(hardwareMap, DcMotor.class, "testLiftDeux");

        claw = loadDevice(hardwareMap, Servo.class, "claw");
        clawRotate1 = loadDevice(hardwareMap, CRServo.class, "clawRotate1");
        clawRotate2 = loadDevice(hardwareMap, CRServo.class, "clawRotate2");
        planeShooter = loadDevice(hardwareMap, Servo.class, "planeShooter");


        arm = loadDevice(hardwareMap, DcMotor.class, "arm");
        spinningIntake = loadDevice(hardwareMap, VirtualMotor.class, "spinningIntake");
	}
}
