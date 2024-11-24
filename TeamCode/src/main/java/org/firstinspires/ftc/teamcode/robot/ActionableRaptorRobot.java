package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;

import lib8812.common.auton.MotorSetPositionAction;
import lib8812.common.auton.ServoSetPositionAction;

public class ActionableRaptorRobot extends RaptorRobot {
    public Action setArmPos(int pos) {
        return new MotorSetPositionAction(arm, pos);
    }

    public Action setClawRotatePos(double pos) {
        return new ServoSetPositionAction(clawRotate, pos);
    }
}
