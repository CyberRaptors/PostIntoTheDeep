package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import lib8812.common.robot.IDriveableRobot;

public class RaptorRobot extends IDriveableRobot {
    public DcMotor leftBack;
    public DcMotor rightBack;
    public DcMotor leftFront;
    public DcMotor rightFront;
    public CRServo swider;

    public void init(HardwareMap hardwareMap) {
        rightFront = loadDevice(hardwareMap, DcMotor.class, "rightFront");
        leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
        rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
        leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");
        swider = loadDevice(hardwareMap, CRServo.class, "swider");
    }
}
