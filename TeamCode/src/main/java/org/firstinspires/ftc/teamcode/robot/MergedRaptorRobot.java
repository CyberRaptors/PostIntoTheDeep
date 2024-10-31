package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.IDriveableRobot;
import lib8812.common.robot.hardwarewrappers.ServoLikeMotor;

public class MergedRaptorRobot extends IDriveableRobot {
    public final int ARM_HANG_MAX_TICKS = 4820;
    public final int ARM_MAX_TICKS = 4530;
    static final int ARM_MIN_TICKS = 0;
    static final int LIFT_MAX_TICKS = 2000;
    static final int LIFT_MIN_TICKS = 0;

    public final double CLAW_ROTATE_MIN_POS = 0.3;
    public final double CLAW_ROTATE_MAX_POS = 1;

    final double INTAKE_SMALL_DIAMETER = 3.75;
    final double INTAKE_LARGE_DIAMETER = 4.75;


    /* MATH - DO NOT TOUCH */
    final double INTAKE_SMALL_RADIUS = INTAKE_SMALL_DIAMETER/2;
    final double INTAKE_LARGE_RADIUS = INTAKE_LARGE_DIAMETER/2;

    public final double INTAKE_SMALL_TO_LARGE_RADIUS_RATIO = INTAKE_SMALL_RADIUS/INTAKE_LARGE_RADIUS;

    public final int INTAKE_SMALL_IN_DIRECTION = 1;
    public final int INTAKE_SMALL_OUT_DIRECTION = -INTAKE_SMALL_IN_DIRECTION;
    public final int INTAKE_LARGE_IN_DIRECTION = INTAKE_SMALL_OUT_DIRECTION;
    public final int INTAKE_LARGE_OUT_DIRECTION = INTAKE_SMALL_IN_DIRECTION;

    /* Hardware Devices */

    public DcMotor actuator;

    public ServoLikeMotor arm;
    public ServoLikeMotor extensionLift;

    public Servo clawRotate;
    public CRServo intakeSmall;
    public CRServo intakeLarge;

    public void init(HardwareMap hardwareMap) {
        rightFront = loadDevice(hardwareMap, DcMotor.class, "rightFront");
        leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
        rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
        leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");

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
        intakeSmall = loadDevice(hardwareMap, CRServo.class, "intake0");
        intakeLarge = loadDevice(hardwareMap, CRServo.class, "intake1");

        clawRotate.setPosition(CLAW_ROTATE_MAX_POS);

        actuator = loadDevice(hardwareMap, DcMotor.class, "actuator0");
    }
}
