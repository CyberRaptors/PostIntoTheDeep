package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;

import lib8812.common.auton.InitAndPredicateAction;
import lib8812.common.auton.MotorSetPositionAction;
import lib8812.common.auton.ServoSetPositionAction;

public class ActionableRaptorRobot extends RaptorRobot {
    public Action setArmPos(int pos) {
        return new MotorSetPositionAction(arm, pos);
    }

    public Action setExtensionLiftPos(int pos) {
        return new MotorSetPositionAction(extensionLift, pos);
    }

    public Action setClawRotatePos(double pos) {
        return new ServoSetPositionAction(clawRotate, pos);
    }

    public Action standardFrog() {
        double alphaDeg = ARM_MAX_ROTATION_DEG * arm.getPosition() / arm.maxPos;
        double thetaDeg = 270 - alphaDeg;
        double theta = Math.toRadians(thetaDeg);

        double liftToGroundExtIn = (ARM_JOINT_MOUNT_HEIGHT_IN / Math.cos(theta)) - ARM_APPROX_LEN_IN;

        int liftToGroundExtTicksReal = (int) Math.floor(liftToGroundExtIn * LIFT_TICKS_PER_INCHES);

        int liftToGroundExtTicksEnsure = liftToGroundExtTicksReal + 75;

        return new SequentialAction(
                new InstantAction(() -> {
                    intakeLarge.setPower(INTAKE_LARGE_IN_DIRECTION);
                    intakeSmall.setPower(INTAKE_SMALL_IN_DIRECTION);
                    lilRaptor.setPosition(LIL_RAPTOR_OUT_POS);
                }),
                new MotorSetPositionAction(extensionLift, liftToGroundExtTicksEnsure), // be safe to not violate the extension limit
                new InstantAction(() -> lilRaptor.setPosition(LIL_RAPTOR_REST_POS)),
                new MotorSetPositionAction(extensionLift, extensionLift.minPos + 50), // we do 0+50 position to reduce risk of causing a macro deadlock
                new InstantAction(() -> {
                    intakeLarge.setPower(0);
                    intakeSmall.setPower(0);
                })
        );
    }

    public Action prepareArmForBackDrop() {
        return new SequentialAction(
                new InitAndPredicateAction(
                        () -> arm.setPosition(REVERSE_DROP_MACRO_ARM_POS),
                        () -> extensionLift.maxPos >= REVERSE_DROP_MACRO_LIFT_POS
                ),
                new MotorSetPositionAction(extensionLift, REVERSE_DROP_MACRO_LIFT_POS),
                new MotorSetPositionAction(arm, REVERSE_DROP_MACRO_ARM_POS) // run this setPosition again for the arm to wait for it to resolve it's position fully
        );
    }

    public Action spitSample() {
        return new SequentialAction(
                new InstantAction(() -> {
                    intakeLarge.setPower(INTAKE_SMALL_OUT_DIRECTION);
                    intakeSmall.setPower(INTAKE_SMALL_OUT_DIRECTION);
                }),
                new SleepAction(2),
                new InstantAction(() -> {
                    intakeLarge.setPower(INTAKE_SMALL_OUT_DIRECTION);
                    intakeSmall.setPower(INTAKE_SMALL_OUT_DIRECTION);
                })
        );
    }
}
