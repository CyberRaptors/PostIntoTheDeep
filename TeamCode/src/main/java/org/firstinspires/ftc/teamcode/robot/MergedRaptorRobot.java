package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.IDriveableRobot;
import lib8812.common.robot.hardwarewrappers.ServoLikeMotor;

public class MergedRaptorRobot extends IDriveableRobot {
    static final int ARM_MAX_TICKS = 4530;
    static final int ARM_MIN_TICKS = 0;
    static final int LIFT_MAX_TICKS = 2000;
    static final int LIFT_MIN_TICKS = 0;

    public final double CLAW_ROTATE_MIN_POS = 0;
    public final double CLAW_ROTATE_MAX_POS = 0.1;

    public Servo clawRotate;

    public DcMotor spinningIntake;
    public DcMotor actuator;

    public ServoLikeMotor arm;
    public ServoLikeMotor extensionLift;

    public void init(HardwareMap hardwareMap) {
        rightFront = loadDevice(hardwareMap, DcMotor.class, "rightFront");
        leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
        rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
        leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");

        spinningIntake = loadDevice(hardwareMap, DcMotor.class, "intake");

        arm = new ServoLikeMotor(
                loadDevice(hardwareMap, DcMotorEx.class, "arm0"),
                ARM_MIN_TICKS, ARM_MAX_TICKS
        );

        extensionLift = new ServoLikeMotor(
                loadDevice(hardwareMap, DcMotorEx.class, "lift0"),
                LIFT_MIN_TICKS, LIFT_MAX_TICKS
        );

        extensionLift.reverse();

        clawRotate = loadDevice(hardwareMap, Servo.class, "clawRotate");

        actuator = loadDevice(hardwareMap, DcMotor.class, "actuator0");
    }
}
