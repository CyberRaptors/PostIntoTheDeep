package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.hardware.DcMotor;

import lib8812.common.actions.MotorSetPositionAction;
import lib8812.common.actions.OnceAction;
import lib8812.common.actions.ServoSetPositionAction;

public class ActionableRaptorRobot extends RaptorRobot {
    public Action setArmPos(int pos) {
        return new MotorSetPositionAction(arm, pos, 20, "arm"); // arm takes more torque so we set a slightly larger window
    }

    public Action setMaxArmPos() { return setArmPos(arm.maxPos); }

    public Action setExtensionLiftPos(int pos) {
        return new MotorSetPositionAction(extensionLift, pos, 15, "lift");
    }

    public Action forceSetExtensionLiftMinPos() {
        return new SequentialAction(
                new MotorSetPositionAction(extensionLift, extensionLift.minPos, 15, "lift", true),
                new InstantAction(() -> { // force lift to accept it is at zero
                    extensionLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    extensionLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                })
        );
    }

    public Action setClawRotatePos(double pos) {
        return new ServoSetPositionAction(clawRotate, pos);
    }

    public Action armMostlyPerpendicular() {
        return setArmPos(ARM_STRAIGHT_UP-60);
    }

    public Action standardFrog() { return standardFrog(0); }

    public Action standardFrog(int manualAdjust) {
        // USE ARM MAX POS ONLY FOR STD FROG

        double alphaDeg = ARM_MAX_ROTATION_DEG;
        double thetaDeg = 270 - alphaDeg;
        double theta = Math.toRadians(thetaDeg);

        double liftToGroundExtIn = (ARM_JOINT_MOUNT_HEIGHT_IN / Math.cos(theta)) - ARM_APPROX_LEN_IN;

        int liftToGroundExtTicksReal = (int) Math.floor(liftToGroundExtIn * LIFT_TICKS_PER_INCHES);

        int liftToGroundExtTicksEnsure = liftToGroundExtTicksReal + (int) (75*NEW_LIFT_TICK_RATIO) + (int) (manualAdjust*NEW_LIFT_TICK_RATIO);

        return new SequentialAction(
                setArmPos(arm.maxPos),
                new InstantAction(() -> {
                    intakeLarge.setPower(INTAKE_LARGE_IN_DIRECTION);
                    intakeSmall.setPower(INTAKE_SMALL_IN_DIRECTION);
                }),
                setExtensionLiftPos(liftToGroundExtTicksEnsure), // be safe to not violate the extension limit
                new SleepAction(0.2),
               setExtensionLiftPos(extensionLift.minPos),
                new InstantAction(() -> {
                    intakeLarge.setPower(0);
                    intakeSmall.setPower(0);
                })
        );
    }

    public Action prepareArmForBackDrop() {
        return new SequentialAction(
                setArmPos(REVERSE_DROP_MACRO_ARM_POS),
                new ParallelAction(
                        setExtensionLiftPos(REVERSE_DROP_MACRO_LIFT_POS),
                        new OnceAction(
                                () -> extensionLift.getPosition() >= 150*NEW_LIFT_TICK_RATIO,
                                setClawRotatePos(CLAW_ROTATE_FORWARDS)
                        )
                )
        );
    }

    public Action asyncBackDropBeginCustom() {
        return new SequentialAction(
                setArmPos(REVERSE_DROP_MACRO_ARM_POS),
                new ParallelAction(
                        new OnceAction(
                                () -> extensionLift.getPosition() >= REVERSE_DROP_MACRO_LIFT_POS-200,
                                new InstantAction(() -> {
                                    intakeSmall.setPower(INTAKE_SMALL_OUT_DIRECTION);
                                    intakeLarge.setPower(INTAKE_LARGE_OUT_DIRECTION*INTAKE_SMALL_TO_LARGE_RADIUS_RATIO);
                                })
                        ),
                        new OnceAction(
                                () -> extensionLift.getPosition() >= 150*NEW_LIFT_TICK_RATIO,
                                setClawRotatePos(CLAW_ROTATE_FORWARDS)
                        ),
                        setExtensionLiftPos(REVERSE_DROP_MACRO_LIFT_POS)
                )
        );
    }

    public Action asyncBackDropEnd() {
        return new SequentialAction(
                new InstantAction(() -> intakeLarge.setPower(0)),
                new InstantAction(() -> {
                    intakeSmall.setPower(0);
                })
        );
    }

    public Action asyncBackDropBegin() {
        return new SequentialAction(
                setArmPos(REVERSE_DROP_MACRO_ARM_POS),
                new ParallelAction(
                        new OnceAction(
                                () -> extensionLift.getPosition() >= REVERSE_DROP_MACRO_LIFT_POS-300,
                                new InstantAction(() -> {
                                    intakeSmall.setPower(INTAKE_SMALL_OUT_DIRECTION);
                                    intakeLarge.setPower(INTAKE_LARGE_OUT_DIRECTION*INTAKE_SMALL_TO_LARGE_RADIUS_RATIO);
                                })
                        ),
                        setExtensionLiftPos(REVERSE_DROP_MACRO_LIFT_POS)
                )
        );
    }

    public Action spitSample(double dt) {
        return new SequentialAction(
                new InstantAction(() -> {
                    intakeLarge.setPower(INTAKE_SMALL_OUT_DIRECTION);
                    intakeSmall.setPower(INTAKE_SMALL_OUT_DIRECTION);
                }),
                new SleepAction(dt),
                new InstantAction(() -> {
                    intakeLarge.setPower(0);
                    intakeSmall.setPower(0);
                })
        );
    }

    public Action spitSample() { return spitSample(1.1); }

    public Action runIntakesIn() {
        return new InstantAction(() -> {
            intakeSmall.setPower(INTAKE_SMALL_IN_DIRECTION);
            intakeLarge.setPower(INTAKE_LARGE_IN_DIRECTION);
        });
    }

    public Action runIntakesOut() {
        return new InstantAction(() -> {
            intakeSmall.setPower(INTAKE_SMALL_OUT_DIRECTION);
            intakeLarge.setPower(INTAKE_LARGE_OUT_DIRECTION);
        });
    }

    public Action idleIntakes() {
        return new InstantAction(() -> {
            intakeSmall.setPower(0);
            intakeLarge.setPower(0);
        });
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
                new SleepAction(0.5),
                new InstantAction(() -> {
                    intakeSmall.setPower(0);
                    intakeLarge.setPower(0);
                })
        );
    }

    public Action fastHangSpecimenBegin() {
        return new SequentialAction(
                new InstantAction(() -> {
                    intakeSmall.setPower(INTAKE_SMALL_IN_DIRECTION);
                    intakeLarge.setPower(INTAKE_LARGE_IN_DIRECTION);
                }),
                setArmPos(BACKWARDS_HIGH_CHAMBER_ARM_POS-125)
        );
    }

    public Action fastHangSpecimenWrap(TrajectoryActionBuilder backupActionBuilder) {
        return new SequentialAction(
                idleIntakes(),
                new ParallelAction(
                        new SequentialAction(
                                new SleepAction(0.5),
                                runIntakesOut()
                        ),
                        backupActionBuilder.build()
                )
        );
    }

    public Action fastHangSpecimenEnd() {
        return new ParallelAction(
                retractArm(),
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
                setExtensionLiftPos((int) (300*NEW_LIFT_TICK_RATIO)),
                setArmPos(BACKWARDS_HIGH_CHAMBER_ARM_POS-300),
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
