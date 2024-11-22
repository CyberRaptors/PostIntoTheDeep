package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.robot.hardwarewrappers.BoundedServo;
import lib8812.common.robot.hardwarewrappers.ServoLikeMotor;

public class AlternateRaptorRobot extends IMecanumRobot {
    static final int ARM_MAX_TICKS = 4530;
    static final int ARM_MIN_TICKS = 0;

    static final double CLAW_ROTATE_MIN_POS = 0;
    static final double CLAW_ROTATE_MAX_POS = 0.5;
    static final double CLAW_ROTATE_INCREMENT = 0.0005;

    static final double EXTENSION_MIN_POS = 0;
    static final double EXTENSION_MAX_POS = 0.67;

    public BoundedServo clawRotate;
    public CRServo spinningIntake;
    public ServoLikeMotor arm;
    public BoundedServo extension;

    public void init(HardwareMap hardwareMap) {
        rightFront = loadDevice(hardwareMap, DcMotor.class, "rightFront");
        leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
        rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
        leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");

        spinningIntake = loadDevice(hardwareMap, CRServo.class, "intake");

        arm = new ServoLikeMotor(
                loadDevice(hardwareMap, DcMotorEx.class, "arm"),
                ARM_MIN_TICKS, ARM_MAX_TICKS
        );

        clawRotate = new BoundedServo(loadDevice(hardwareMap, Servo.class, "clawRotate"))
                .attachBounds(CLAW_ROTATE_MIN_POS, CLAW_ROTATE_MAX_POS)
                .attachIncrement(CLAW_ROTATE_INCREMENT);

        extension = new BoundedServo(loadDevice(hardwareMap, Servo.class, "extension"))
                .attachBounds(EXTENSION_MIN_POS, EXTENSION_MAX_POS);
    }
}
