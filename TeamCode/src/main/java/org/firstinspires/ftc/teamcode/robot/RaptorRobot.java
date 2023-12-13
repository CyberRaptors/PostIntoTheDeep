package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.LabeledPositionServo;
import lib8812.common.robot.ServoLikeMotor;
import lib8812.common.robot.VirtualMotor;
import lib8812.common.robot.IDriveableRobot;
import lib8812.common.robot.uapi.IPixelManager;

public class RaptorRobot extends IDriveableRobot {
    public final double CLAW_ONE_OPEN = 0.63;
    public final double CLAW_ONE_CLOSED = 0.53;
    public final double CLAW_TWO_CLOSED = 0.49;
    public final double CLAW_TWO_OPEN = 0.4;
    public final double PLANE_SHOT = 0.5;
    public final double PLANE_READY = 0.7;
    public final double GOBUILDA_117_TICKS_PER_REV = 1425.1;
    public final int ARM_MAX_TICKS = 798; // needs to be figured out empirically
    public final int ARM_MIN_TICKS = 0;

    public DcMotor leftBack;
    public DcMotor rightBack;
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor actuator;

    public LabeledPositionServo clawOne;
    public LabeledPositionServo clawTwo;
    public Servo clawRotate;
    // mechanical devices not added yet
    public ServoLikeMotor arm;
    public LabeledPositionServo planeShooter;
    public VirtualMotor spinningIntake;

    public IPixelManager pixelManager;

    public void init(HardwareMap hardwareMap) {
		rightFront  = loadDevice(hardwareMap, DcMotor.class, "rightFront");
		leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
		rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
		leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");

        actuator = loadDevice(hardwareMap, DcMotor.class, "actuator");

        clawOne = new LabeledPositionServo(
                loadDevice(hardwareMap, Servo.class, "clawOne"),
                new String[] { "CLOSED", "OPEN" },
                new Double[] { CLAW_ONE_CLOSED, CLAW_ONE_OPEN }
        );

        clawTwo = new LabeledPositionServo(
                loadDevice(hardwareMap, Servo.class, "clawTwo"),
                new String[] { "CLOSED", "OPEN" },
                new Double[] { CLAW_TWO_CLOSED, CLAW_TWO_OPEN }
        );

        clawRotate = loadDevice(hardwareMap, Servo.class, "clawRotate");

        planeShooter = new LabeledPositionServo(
                loadDevice(hardwareMap, Servo.class, "planeShooter"),
                new String[] { "READY", "SHOT"},
                new Double[] { PLANE_READY, PLANE_SHOT }
        );

        arm = new ServoLikeMotor(loadDevice(hardwareMap, DcMotor.class, "arm"), ARM_MIN_TICKS, ARM_MAX_TICKS);

        spinningIntake = loadDevice(hardwareMap, VirtualMotor.class, "spinningIntake");

        clawRotate.setPosition(0.77);
        clawOne.setLabeledPosition("CLOSED");
        clawTwo.setLabeledPosition("CLOSED");
        arm.setDirection(DcMotorSimple.Direction.REVERSE);

        pixelManager = new IPixelManager() {
            int numPixels = 2;

            @Override
            public void intakeOneFront() throws UnsupportedOperationException {
                if (numPixels == 2) throw new UnsupportedOperationException();

                if (numPixels == 1) clawOne.setLabeledPosition("CLOSED");
                else clawTwo.setLabeledPosition("CLOSED");

                numPixels++;
            }

            @Override
            public void intakeOneBack() throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void releaseOneFront() throws UnsupportedOperationException {
                if (numPixels == 0) throw new UnsupportedOperationException();

                if (numPixels == 2) clawOne.setLabeledPosition("OPEN");
                else clawTwo.setLabeledPosition("OPEN");

                numPixels--;
            }

            @Override
            public void releaseOneBack() throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void releaseAutonOneFront() {
                clawTwo.setLabeledPosition("OPEN");
                numPixels--;
            }

            @Override
            public void releaseAutonTwoFront() {
                clawOne.setLabeledPosition("OPEN");
                numPixels--;
            }
        };
	}
}
