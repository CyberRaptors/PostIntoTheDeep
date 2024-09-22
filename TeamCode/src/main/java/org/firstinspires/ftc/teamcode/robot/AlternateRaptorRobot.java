package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.BinaryClaw;
import lib8812.common.robot.IDriveableRobot;
import lib8812.common.robot.ServoLikeMotor;

public class AlternateRaptorRobot extends IDriveableRobot {
    static final int ARM_MAX_TICKS = 1500;
    static final int ARM_MIN_TICKS = 0;

    public Servo clawRotate;

    public CRServo spinningIntake;

    public ServoLikeMotor arm;

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

        clawRotate = loadDevice(hardwareMap, Servo.class, "clawRotate");
    }
}
