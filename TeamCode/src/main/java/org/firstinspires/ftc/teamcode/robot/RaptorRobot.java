package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.robot.hardwarewrappers.ServoLikeMotor;
import lib8812.common.rr.SparkFunOTOSDrive;

public class RaptorRobot extends IMecanumRobot {
    public final int ARM_HANG_MAX_TICKS = 4820;
    public final int ARM_MAX_TICKS = 4530;
    public final double ARM_MAX_ROTATION_DEG = 230d;
    public final double ARM_APPROX_LEN_IN = 14;
    public final double ARM_JOINT_MOUNT_HEIGHT_IN = 16;

    static final int ARM_MIN_TICKS = 0;
    public final int LIFT_MAX_TICKS = 2000;
    public final int LIFT_MIN_TICKS = 0;
    static final double LIFT_MAX_INCHES = 25.7;
    static final double LIFT_MIN_INCHES = 0;

    public final double LIFT_TICKS_PER_INCHES = ((double) LIFT_MAX_TICKS-LIFT_MIN_TICKS)/(LIFT_MAX_INCHES-LIFT_MIN_INCHES);

    public final double CLAW_ROTATE_MIN_POS = 0.3;
    public final double CLAW_ROTATE_MAX_POS = 1;
    public final double CLAW_ROTATE_FORWARDS = (CLAW_ROTATE_MIN_POS+CLAW_ROTATE_MAX_POS)/2;

    public final double LIL_RAPTOR_REST_POS = 0;
    public final double LIL_RAPTOR_OUT_POS = 0.3;

    final double INTAKE_SMALL_DIAMETER = 3.75;
    final double INTAKE_LARGE_DIAMETER = 4.75;


    /* MATH - DO NOT TOUCH */
    final double INTAKE_SMALL_RADIUS = INTAKE_SMALL_DIAMETER/2;
    final double INTAKE_LARGE_RADIUS = INTAKE_LARGE_DIAMETER/2;
    public final double INTAKE_SMALL_TO_LARGE_RADIUS_RATIO = INTAKE_SMALL_RADIUS/INTAKE_LARGE_RADIUS;


    /* INTAKE DIRECTIONS */
    public final int INTAKE_SMALL_IN_DIRECTION = 1;
    public final int INTAKE_SMALL_OUT_DIRECTION = -INTAKE_SMALL_IN_DIRECTION;
    public final int INTAKE_LARGE_IN_DIRECTION = INTAKE_SMALL_OUT_DIRECTION;
    public final int INTAKE_LARGE_OUT_DIRECTION = INTAKE_SMALL_IN_DIRECTION;

    /* MACRO CONSTANTS [specified here to allow use in auton] */

    public final int REVERSE_DROP_MACRO_ARM_POS = 1562;
    public final int REVERSE_DROP_MACRO_LIFT_POS = 1585;
    public final int BACKWARDS_HIGH_CHAMBER_ARM_POS = 1258;
    public final int FORWARDS_HIGH_BASKET_ARM_POS = 2215;
    public final int FORWARDS_HIGH_BASKET_LIFT_POS = 1538;
    public final int AUTON_ASCENT_ARM_POS = 1000;


    /* Hardware Devices */

    public ServoLikeMotor arm;
    public ServoLikeMotor extensionLift;

    public Servo clawRotate;
    public CRServo intakeSmall;
    public CRServo intakeLarge;
    public Servo lilRaptor;

    public SparkFunOTOSDrive drive;

    public void init(HardwareMap hardwareMap) {
        rightFront = loadDevice(hardwareMap, DcMotor.class, "rightFront");
        leftFront = loadDevice(hardwareMap, DcMotor.class, "leftFront");
        rightBack = loadDevice(hardwareMap, DcMotor.class, "rightBack");
        leftBack = loadDevice(hardwareMap, DcMotor.class, "leftBack");

        rightFront.resetDeviceConfigurationForOpMode();
        leftFront.resetDeviceConfigurationForOpMode();
        rightBack.resetDeviceConfigurationForOpMode();
        leftBack.resetDeviceConfigurationForOpMode();

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        arm = new ServoLikeMotor(
                loadDevice(hardwareMap, DcMotorEx.class, "arm0"),
                ARM_MIN_TICKS, ARM_MAX_TICKS
        );

        extensionLift = new ServoLikeMotor(
                loadDevice(hardwareMap, DcMotorEx.class, "lift0"),
                LIFT_MIN_TICKS, LIFT_MAX_TICKS
        );

        extensionLift.reverse();

        clawRotate = loadDevice(hardwareMap, Servo.class, "clawRotate");
        clawRotate.resetDeviceConfigurationForOpMode();

        intakeSmall = loadDevice(hardwareMap, CRServo.class, "intake0");
        intakeSmall.resetDeviceConfigurationForOpMode();

        intakeLarge = loadDevice(hardwareMap, CRServo.class, "intake1");
        intakeLarge.resetDeviceConfigurationForOpMode();

        lilRaptor = loadDevice(hardwareMap, Servo.class, "lilRaptor");
        lilRaptor.resetDeviceConfigurationForOpMode();

        clawRotate.setPosition(CLAW_ROTATE_MAX_POS);
        lilRaptor.setPosition(LIL_RAPTOR_REST_POS);

        drive = new SparkFunOTOSDrive(hardwareMap, new Pose2d(0, 0, 0));
    }

    public void setRRDrivePose(Pose2d pose) {
        drive.updatePoseEstimate();
        drive.pose = pose;
    }
}
