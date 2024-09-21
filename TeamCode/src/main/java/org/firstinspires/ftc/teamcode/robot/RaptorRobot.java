package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.BinaryClaw;
import lib8812.common.robot.IDriveableRobot;
import lib8812.common.robot.ServoLikeMotor;
import lib8812.common.robot.VirtualMotor;

public class RaptorRobot extends IDriveableRobot {
    static final int LIFT_MAX_TICKS = 3100;
    static final int LIFT_MIN_TICKS = 0;
    static final double CLAW_OPEN_POS = 0.5;
    static final double CLAW_CLOSED_POS = 0;
    public final double CLAW_ROTATE_MIN_POS = 0;
    public final double CLAW_ROTATE_MAX_POS = 0.1;

    public Servo extension;
    public Servo clawRotate;

    public CRServo spinningIntake;

    public ServoLikeMotor mainLift;
    public BinaryClaw claw;

    public void init(HardwareMap hardwareMap) {
        rightFront = loadDevice(hardwareMap, DcMotor.class, "rightFront");
        leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
        rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
        leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");
        extension = loadDevice(hardwareMap, Servo.class, "extension");
        spinningIntake = loadDevice(hardwareMap, CRServo.class, "intake");
        mainLift = new ServoLikeMotor(
                loadDevice(hardwareMap, DcMotorEx.class, "lift0"),
                LIFT_MIN_TICKS, LIFT_MAX_TICKS
        );

        clawRotate = loadDevice(hardwareMap, Servo.class, "clawRotate");
        claw = new BinaryClaw(
                loadDevice(hardwareMap, Servo.class, "claw"),
                CLAW_OPEN_POS, CLAW_CLOSED_POS
        );
    }
}
