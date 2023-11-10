package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.VirtualMotor;
import lib8812.common.robot.VirtualServo;
import lib8812.common.teleop.IDriveableRobot;

public class RaptorRobot extends IDriveableRobot {
    public final double CLAW_OPEN = 0.3  ;
    public final double CLAW_CLOSED = 0.2;

    public DcMotor leftBack;
    public DcMotor rightBack;
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor planeLauncher;
    public DcMotor testLift1;
    public DcMotor testLift2;
    public CRServo hangServoLeft;
    public CRServo hangServoRight;
    public Servo planePush;
    public Servo claw;

    // mechanical devices not added yet
    public VirtualServo arm;
    public VirtualMotor spinningIntake;

    public void init(HardwareMap hardwareMap) {
		rightFront  = loadDevice(hardwareMap, DcMotor.class, "rightFront");
		leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
		rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
		leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");

        planeLauncher = loadDevice(hardwareMap, DcMotor.class, "lePlaneLauncher");

        testLift1 = loadDevice(hardwareMap, DcMotor.class, "testLift");
        testLift2 = loadDevice(hardwareMap, DcMotor.class, "testLiftDeux");

        hangServoLeft = loadDevice(hardwareMap, CRServo.class, "hangServoLeft");
        hangServoRight = loadDevice(hardwareMap, CRServo.class, "hangServoRight");

        planePush = loadDevice(hardwareMap, Servo.class, "planePush");
        claw = loadDevice(hardwareMap, Servo.class, "claw");


        arm = loadDevice(hardwareMap, VirtualServo.class, "arm");
        spinningIntake = loadDevice(hardwareMap, VirtualMotor.class, "spinningIntake");
	}
}
