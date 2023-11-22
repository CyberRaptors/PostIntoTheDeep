package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.LabeledPositionServo;
import lib8812.common.robot.ServoLikeMotor;
import lib8812.common.robot.VirtualMotor;
import lib8812.common.teleop.IDriveableRobot;

public class RaptorRobot extends IDriveableRobot {
    public final double CLAW_ONE_OPEN = 1;
    public final double CLAW_CLOSED = 0.5;
    public final double CLAW_TWO_OPEN = 0;

    public final double PLANE_SHOT = 0.5;
    public final double PLANE_READY = 0.7;
    public final int REV_CORE_HEX_TICKS_PER_REV = 288;
    public final int ARM_MAX_TICKS = REV_CORE_HEX_TICKS_PER_REV*2/3; // needs to be figured out empirically
    public final int ARM_MIN_TICKS = 0;

    public DcMotor leftBack;
    public DcMotor rightBack;
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor actuator;

    public LabeledPositionServo clawOne;
    public LabeledPositionServo clawTwo;
    public Servo clawRotate;
    // mechanical devices not added yet
    public ServoLikeMotor arm;
    public LabeledPositionServo planeShooter;
    public VirtualMotor spinningIntake;

    public void init(HardwareMap hardwareMap) {
		rightFront  = loadDevice(hardwareMap, DcMotor.class, "rightFront");
		leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
		rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
		leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");

        actuator = loadDevice(hardwareMap, DcMotor.class, "actuator");

        clawOne = new LabeledPositionServo(
                loadDevice(hardwareMap, Servo.class, "clawOne"),
                new String[] { "CLOSED", "OPEN" },
                new Double[] { CLAW_CLOSED, CLAW_ONE_OPEN }
        );

        clawTwo = new LabeledPositionServo(
                loadDevice(hardwareMap, Servo.class, "clawTwo"),
                new String[] { "CLOSED", "OPEN" },
                new Double[] { CLAW_CLOSED, CLAW_TWO_OPEN }
        );

        clawRotate = loadDevice(hardwareMap, Servo.class, "clawRotate");

        planeShooter = new LabeledPositionServo(
                loadDevice(hardwareMap, Servo.class, "planeShooter"),
                new String[] { "READY", "SHOT"},
                new Double[] { PLANE_READY, PLANE_SHOT }
        );

        arm = new ServoLikeMotor(loadDevice(hardwareMap, DcMotor.class, "arm"), ARM_MIN_TICKS, ARM_MAX_TICKS);

        spinningIntake = loadDevice(hardwareMap, VirtualMotor.class, "spinningIntake");
	}
}
