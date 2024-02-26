package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


import java.util.function.Consumer;

import lib8812.common.robot.LabeledPositionServo;
import lib8812.common.robot.ServoLikeMotor;
import lib8812.common.robot.IDriveableRobot;
import lib8812.common.robot.uapi.IPixelManager;

public class RaptorRobot extends IDriveableRobot {
    public final double PLANE_SHOT = 0.5;
    public final double PLANE_READY = 0.9;
    public final double GOBILDA_117_RPM_TICKS_PER_REV = 1425.1;
    public final double GOBILDA_30_RPM_TICKS_PER_REV = 5291.1;
    public final double ARM_MOTOR_TICKS_PER_REV = GOBILDA_30_RPM_TICKS_PER_REV;
    public final double ARM_MAX_ROTATION = 0.531080493659163;
    public final double MAX_EXTEND = 0.59;
    public final double MIN_EXTEND = 0.05;
    public final double CLAW_ROTATE_MIN = 0.54;
    public final double CLAW_ROTATE_MAX = 0.64;
    public final int ARM_MAX_TICKS = (int) (ARM_MOTOR_TICKS_PER_REV*ARM_MAX_ROTATION);
    public final int ARM_MIN_TICKS = 0;

    public final double CLAW_ROTATE_PARALLEL_FOR_PICKUP_MOVEMENT = 0.6022;
    public final double CLAW_ROTATE_REST_OVER_WHEELS = 0.6144;
    public final double CLAW_ROTATE_OPTIMAL_PICKUP = 0.6088;
    public final double CLAW_ROTATE_GUARANTEED_PICKUP = 0.6090;
    public final double CLAW_ROTATE_OVER_PLANE_LAUNCHER_POS = CLAW_ROTATE_MAX;
    public final double AUTON_FROZEN_CLAW_ROTATE = 0.5811;
    public final int AUTON_FROZEN_ARM_TICKS = 2106;
    public final double STANDARD_DROP_CLAW_ROTATE = 0.5978;
    public final int STANDARD_DROP_ARM_TICKS = 1926;
    public final double PRECISION_DROP_CLAW_ROTATE = 0.5900;
    public final int PRECISION_DROP_ARM_TICKS = 2106;


    public DcMotor leftBack;
    public DcMotor rightBack;
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor actuator;
    public Servo clawRotate;
    // mechanical devices not added yet
    public ServoLikeMotor arm;
    public LabeledPositionServo planeShooter;
    public CRServo spinOne;
    public CRServo spinTwo;
    public IPixelManager pixelManager;
    public Servo extendRail;

    public void init(HardwareMap hardwareMap) {
		rightFront  = loadDevice(hardwareMap, DcMotor.class, "rightFront");
		leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
		rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
		leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");

        actuator = loadDevice(hardwareMap, DcMotor.class, "actuator");

        extendRail = loadDevice(hardwareMap, Servo.class, "extendRail");

        clawRotate = loadDevice(hardwareMap, Servo.class, "clawRotate");

        planeShooter = new LabeledPositionServo(
                loadDevice(hardwareMap, Servo.class, "planeShooter"),
                new String[] { "READY", "SHOT"},
                new Double[] { PLANE_READY, PLANE_SHOT }
        );

        arm = new ServoLikeMotor(loadDevice(hardwareMap, DcMotor.class, "arm"), ARM_MIN_TICKS, ARM_MAX_TICKS);

        spinOne = loadDevice(hardwareMap, CRServo.class, "spinOne");
        spinTwo = loadDevice(hardwareMap, CRServo.class, "spinTwo");

        clawRotate.setPosition(CLAW_ROTATE_MAX);
//        clawOne.setLabeledPosition("CLOSED");
//        clawTwo.setLabeledPosition("CLOSED");
        arm.setDirection(DcMotorSimple.Direction.REVERSE);
        arm.enableAlgorithmAutomatic("anti-stress");

        planeShooter.setLabeledPosition("READY");

        extendRail.setPosition(MAX_EXTEND);

        pixelManager = new IPixelManager() {
            int numPixels = 2;
            Consumer<Long> sleep;

            @Override
            public void init(Consumer<Long> sleepFunc)
            {
                sleep = sleepFunc;
            }

            @Override
            public void intakeOneFront() throws UnsupportedOperationException {
                if (numPixels == 2) throw new UnsupportedOperationException();

//                if (numPixels == 1) clawOne.setLabeledPosition("CLOSED");
//                else clawTwo.setLabeledPosition("CLOSED");


                numPixels++;
            }

            @Override
            public void intakeOneBack() throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void releaseOneFront() throws UnsupportedOperationException {
                if (numPixels == 0) throw new UnsupportedOperationException();

//                if (numPixels == 2) clawOne.setLabeledPosition("OPEN");
//                else clawTwo.setLabeledPosition("OPEN");

                numPixels--;
            }

            @Override
            public void releaseOneBack() throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void releaseAutonOneFront() {
                spinOne.setPower(-0.4); // spinOne -> yellow
                spinTwo.setPower(-0.4); // spinTwo -> purple
                sleep.accept(650L);
                spinOne.setPower(0);
                sleep.accept(500L);
                spinTwo.setPower(0);

                spinOne.setPower(-0.7);
                sleep.accept(100L);
                spinOne.setPower(0);


                numPixels--;
            }

            @Override
            public void releaseAutonTwoFront() {
                spinOne.setPower(0.25); // negative this time because dropping from other side evn though this technically violates the implications of releaseOne**Front**
                spinTwo.setPower(0.25);
                sleep.accept(1000l);
                spinOne.setPower(0);
                spinTwo.setPower(0);

                numPixels--;
            }
        };
	}
}
