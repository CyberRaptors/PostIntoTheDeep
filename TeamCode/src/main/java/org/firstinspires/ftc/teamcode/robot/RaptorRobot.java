package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.hardwarewrappers.BinaryClaw;
import lib8812.common.robot.hardwarewrappers.DegreeInchesOTOS;
import lib8812.common.robot.IDriveableRobot;
import lib8812.common.robot.hardwarewrappers.ServoLikeMotor;

public class RaptorRobot extends IDriveableRobot {
    static final int LIFT_MAX_TICKS = 3100;
    static final int LIFT_MIN_TICKS = 0;
    static final double CLAW_OPEN_POS = 0.5;
    static final double CLAW_CLOSED_POS = 0;

    public final double CLAW_ROTATE_MIN_POS = 0;
    public final double CLAW_ROTATE_MAX_POS = 0.5; // temp for claw slipping

    public final double EXTENSION_MAX_POS = 0.67;

    public Servo extension;
    public Servo clawRotate;

    public ServoLikeMotor mainLift;
    public BinaryClaw claw;

    public DcMotorEx pushActuator;

    public DegreeInchesOTOS otos;
    final DegreeInchesOTOS.Configuration otosConfig =
            new DegreeInchesOTOS.Configuration()
                    .withOffset(
                            0, 0, 0
                    )
                    .withStartingPoint(
                            0, 0, 0
                    )
                    .withLinearMultiplier(1)
                    .withAngularMultiplier(1);

    public void init(HardwareMap hardwareMap) {
        rightFront = loadDevice(hardwareMap, DcMotor.class, "rightFront");
        leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
        rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
        leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");
        extension = loadDevice(hardwareMap, Servo.class, "extension");
        mainLift = new ServoLikeMotor(
                loadDevice(hardwareMap, DcMotorEx.class, "lift0"),
                LIFT_MIN_TICKS, LIFT_MAX_TICKS
        );

        pushActuator = loadDevice(hardwareMap, DcMotorEx.class, "actuator0");

        clawRotate = loadDevice(hardwareMap, Servo.class, "clawRotate");
        claw = new BinaryClaw(
                loadDevice(hardwareMap, Servo.class, "claw"),
                CLAW_OPEN_POS, CLAW_CLOSED_POS
        );

        otos = new DegreeInchesOTOS(
                loadDevice(hardwareMap, SparkFunOTOS.class, "otos"),
                otosConfig
        );

        claw.close();
//        clawRotate.setPosition(CLAW_ROTATE_MIN_POS);
    }
}
