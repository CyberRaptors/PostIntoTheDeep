package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.ServoLikeMotor;
import lib8812.common.robot.VirtualMotor;
import lib8812.common.teleop.IDriveableRobot;

public class RaptorRobot extends IDriveableRobot {
    public final double CLAW_ONE_OPEN = 1;
    public final double CLAW_CLOSED = 0.5;
    public final double CLAW_TWO_OPEN = 0;

    public final double PLANE_SHOT = 0.5;
    public final double PLANE_READY = 0.7;

    public DcMotor leftBack;
    public DcMotor rightBack;
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor actuator;

    public Servo clawOne;
    public Servo clawTwo;
    public Servo clawRotate;
    // mechanical devices not added yet
    public ServoLikeMotor arm;
    public Servo planeShooter;
    public VirtualMotor spinningIntake;

    public void init(HardwareMap hardwareMap) {
		rightFront  = loadDevice(hardwareMap, DcMotor.class, "rightFront");
		leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
		rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
		leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");

        actuator = loadDevice(hardwareMap, DcMotor.class, "actuator");

        clawOne = loadDevice(hardwareMap, Servo.class, "clawOne");
        clawTwo = loadDevice(hardwareMap, Servo.class, "clawTwo");
        clawRotate = loadDevice(hardwareMap, Servo.class, "clawRotate");

        planeShooter = loadDevice(hardwareMap, Servo.class, "planeShooter");

        arm = new ServoLikeMotor(loadDevice(hardwareMap, DcMotor.class, "arm"), 0, 3000);

        spinningIntake = loadDevice(hardwareMap, VirtualMotor.class, "spinningIntake");
	}
}
