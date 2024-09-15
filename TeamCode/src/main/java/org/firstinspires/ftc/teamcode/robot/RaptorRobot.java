package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.IDriveableRobot;
import lib8812.common.robot.ServoLikeMotor;
import lib8812.common.robot.VirtualMotor;

public class RaptorRobot extends IDriveableRobot {
    static final int LIFT_MAX_TICKS = 2000;
    static final int LIFT_MIN_TICKS = 0;

    public Servo extension;
    public CRServo intake;
    public ServoLikeMotor mainLift;

    public void init(HardwareMap hardwareMap) {
        rightFront = loadDevice(hardwareMap, DcMotor.class, "rightFront");
        leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
        rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
        leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");
        extension = loadDevice(hardwareMap, Servo.class, "extension");
        intake = loadDevice(hardwareMap, CRServo.class, "intake");
        mainLift = new ServoLikeMotor(
                loadDevice(hardwareMap, VirtualMotor.class, "lift0"),
                LIFT_MIN_TICKS, LIFT_MAX_TICKS
        );
    }
}
