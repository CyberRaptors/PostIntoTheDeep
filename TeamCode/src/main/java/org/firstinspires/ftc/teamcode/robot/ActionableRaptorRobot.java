package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;

import lib8812.common.actions.MotorSetPositionAction;
import lib8812.common.actions.ServoSetPositionAction;

public class ActionableRaptorRobot extends RaptorRobot {
    public Action setArmPos(int pos) {
        return new MotorSetPositionAction(arm, pos);
    }

    public Action setMaxArmPos() { return setArmPos(arm.maxPos); }

    public Action setExtensionLiftPos(int pos) {
        return new MotorSetPositionAction(extensionLift, pos);
    }

    public Action setClawRotatePos(double pos) {
        return new ServoSetPositionAction(clawRotate, pos);
    }

    public Action standardFrog() {
        // USE ARM MAX POS ONLY FOR STD FROG

        double alphaDeg = ARM_MAX_ROTATION_DEG;
        double thetaDeg = 270 - alphaDeg;
        double theta = Math.toRadians(thetaDeg);

        double liftToGroundExtIn = (ARM_JOINT_MOUNT_HEIGHT_IN / Math.cos(theta)) - ARM_APPROX_LEN_IN;

        int liftToGroundExtTicksReal = (int) Math.floor(liftToGroundExtIn * LIFT_TICKS_PER_INCHES);

        int liftToGroundExtTicksEnsure = liftToGroundExtTicksReal + 75;

        return new SequentialAction(
                setArmPos(arm.maxPos),
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
                setArmPos(REVERSE_DROP_MACRO_ARM_POS),
                setExtensionLiftPos(REVERSE_DROP_MACRO_LIFT_POS)
        );
    }

    public Action spitSample() {
        return new SequentialAction(
                new InstantAction(() -> {
                    intakeLarge.setPower(INTAKE_SMALL_OUT_DIRECTION);
                    intakeSmall.setPower(INTAKE_SMALL_OUT_DIRECTION);
                }),
                new SleepAction(1.5),
                new InstantAction(() -> {
                    intakeLarge.setPower(0);
                    intakeSmall.setPower(0);
                })
        );
    }

    public Action ascend() {
        return setArmPos(AUTON_ASCENT_ARM_POS);
    }

    public Action clutchPreload() {
        return new SequentialAction(
                new InstantAction(() -> {
                    intakeSmall.setPower(INTAKE_SMALL_IN_DIRECTION);
                    intakeLarge.setPower(INTAKE_LARGE_IN_DIRECTION);

                }),
                new SleepAction(1.5),
                new InstantAction(() -> {
                    intakeSmall.setPower(0);
                    intakeLarge.setPower(0);
                })
        );
    }

    public Action hangPreloadStationary() {
        return new SequentialAction(
                new InstantAction(() -> {
                    /*  clutch the specimen */
                    intakeSmall.setPower(INTAKE_SMALL_IN_DIRECTION);
                    intakeLarge.setPower(INTAKE_LARGE_IN_DIRECTION);
                }),
                /* hook the specimen onto the high chamber and wait for at least 0.5 sec */
                setExtensionLiftPos(700),
                new SleepAction(0.3),
                setArmPos(BACKWARDS_HIGH_CHAMBER_ARM_POS-300),
                new SleepAction(0.3),
                setExtensionLiftPos(extensionLift.minPos),
                new InstantAction(() -> {
                    /* once the specimen is secured to the high chamber, forcefully release it and raise the arm */
                    intakeSmall.setPower(INTAKE_SMALL_OUT_DIRECTION);
                    intakeLarge.setPower(INTAKE_LARGE_OUT_DIRECTION);
                }),
                setArmPos(BACKWARDS_HIGH_CHAMBER_ARM_POS),
                new InstantAction(() -> {
                    intakeSmall.setPower(0);
                    intakeLarge.setPower(0);
                })
        );
    }

    public Action prepareArmForSpecimenHang() {
        return new ParallelAction(
                setArmPos(BACKWARDS_HIGH_CHAMBER_ARM_POS),
                setClawRotatePos(CLAW_ROTATE_FORWARDS)
        );
    }

    public Action retractArm() {
        return setArmPos(arm.minPos);
    }
}
