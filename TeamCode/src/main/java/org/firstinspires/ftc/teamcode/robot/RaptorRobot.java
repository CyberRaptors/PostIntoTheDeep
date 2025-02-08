package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.robot.MecanumLimelightAlgorithms;
import lib8812.common.robot.hardwarewrappers.BinaryClaw;
import lib8812.common.robot.hardwarewrappers.LabeledPositionServo;
import lib8812.common.robot.hardwarewrappers.LimelightManager;
import lib8812.common.robot.hardwarewrappers.ServoLikeMotor;
import lib8812.common.robot.poses.SimpleRadianPose;
import lib8812.common.rr.SparkFunOTOSDrive;

public class RaptorRobot extends IMecanumRobot {
    public final int ARM_HANG_MAX_TICKS = 4820;
    public final int ARM_MAX_TICKS = 4530;
    public final double ARM_MAX_ROTATION_DEG = 230d;
    public final double ARM_APPROX_LEN_IN = 14;
    public final double ARM_JOINT_MOUNT_HEIGHT_IN = 16;

    protected double NEW_LIFT_TICK_RATIO = 537.7/384.5;

    static final int ARM_MIN_TICKS = 0;
    public final int LIFT_MAX_TICKS = (int) (2000*NEW_LIFT_TICK_RATIO);
    public final int LIFT_MIN_TICKS = 0;
    static final double LIFT_MAX_INCHES = 25.7;
    static final double LIFT_MIN_INCHES = 0;

    public final double LIFT_TICKS_PER_INCHES = ((double) LIFT_MAX_TICKS-LIFT_MIN_TICKS)/(LIFT_MAX_INCHES-LIFT_MIN_INCHES);

    public final double CLAW_ROTATE_MIN_POS = 0.3;
    public final double CLAW_ROTATE_MAX_POS = 1;
    public final double CLAW_ROTATE_FORWARDS = (CLAW_ROTATE_MIN_POS+CLAW_ROTATE_MAX_POS)/2 + /* custom offset */ 0.03;

    public final double LIL_RAPTOR_REST_POS = 0;
    public final double LIL_RAPTOR_OUT_POS = 0.3;

    final double INTAKE_SMALL_DIAMETER = 3.75;
    final double INTAKE_LARGE_DIAMETER = 4.75;


    /* MATH - DO NOT TOUCH */
    final double INTAKE_SMALL_RADIUS = INTAKE_SMALL_DIAMETER/2;
    final double INTAKE_LARGE_RADIUS = INTAKE_LARGE_DIAMETER/2;
    public final double INTAKE_SMALL_TO_LARGE_RADIUS_RATIO = INTAKE_SMALL_RADIUS/INTAKE_LARGE_RADIUS;


    /* INTAKE DIRECTIONS */
    public final int INTAKE_SMALL_IN_DIRECTION = -1;
    public final int INTAKE_SMALL_OUT_DIRECTION = -INTAKE_SMALL_IN_DIRECTION;
    public final int INTAKE_LARGE_IN_DIRECTION = INTAKE_SMALL_OUT_DIRECTION;
    public final int INTAKE_LARGE_OUT_DIRECTION = INTAKE_SMALL_IN_DIRECTION;

    /* MACRO CONSTANTS [specified here to allow use in auton] */

    // TODO: WHEN REPLACING LIFT MOTOR, THE CONSTANTS HERE MUST BE CHANGED AS WELL AS LIFT_MAX_TICKS AND ALSO ActionableRaptorRobot's actions which use lift tick constants

    public final int REVERSE_DROP_MACRO_ARM_POS = 1562;
    public final int REVERSE_DROP_MACRO_LIFT_POS = (int) (1585*NEW_LIFT_TICK_RATIO);
    public final int BACKWARDS_HIGH_CHAMBER_ARM_POS = 1258;
    public final int PAST_FORWARDS_HIGH_CHAMBER_ARM_POS = (int) ((160d/ARM_MAX_ROTATION_DEG)*ARM_MAX_TICKS); // TODO: fix
    public final int FORWARDS_HIGH_BASKET_ARM_POS = 2215;
    public final int FORWARDS_HIGH_BASKET_LIFT_POS = (int) (1538*NEW_LIFT_TICK_RATIO);
    public final int AUTON_ASCENT_ARM_POS = 1000;
    public final int ARM_STRAIGHT_UP = 1856;
    public final int ARM_PICKUP_FROM_BACK_WALL = 220;


    final static double LIMELIGHT_ALIGN_TARGET_SAMPLE_SIZE = 0;

    /* Hardware Devices */

    public ServoLikeMotor arm;
    public ServoLikeMotor extensionLift;

    public Servo clawRotate;
    public CRServo intakeSmall;
    public CRServo intakeLarge;
    public Servo lilRaptor;

    public LabeledPositionServo auxClawRotate;
    public BinaryClaw auxClaw;

    public SparkFunOTOSDrive drive;

    public LimelightManager limelightMgr;
    public MecanumLimelightAlgorithms limelightAlgorithms;


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

        auxClawRotate = new LabeledPositionServo(
                loadDevice(hardwareMap, Servo.class, "auxClawRotate"),
                new String[] { "up", "down" },
                new Double[] { 0.025, 0.3 }
        );

        auxClaw = new BinaryClaw(
                loadDevice(hardwareMap, Servo.class, "auxClaw"),
                0.35,
                0.1
        );

        auxClaw.inner.addPositionLabel("power saving mode", 0.550);

        auxClaw.inner.setDirection(Servo.Direction.REVERSE);


        clawRotate.setPosition(CLAW_ROTATE_MAX_POS);
        lilRaptor.setPosition(LIL_RAPTOR_REST_POS);

        auxClawRotate.setLabeledPosition("up");
        auxClaw.inner.setLabeledPosition("power saving mode");


        drive = new SparkFunOTOSDrive(hardwareMap, new Pose2d(0, 0, 0));


        limelightMgr = new LimelightManager(
                hardwareMap.get(Limelight3A.class, "limelight"),
                new SimpleRadianPose(0, 0, 0)
        );

        limelightMgr.pipelineSwitch(1);

        limelightAlgorithms = new MecanumLimelightAlgorithms(drive, limelightMgr, LIMELIGHT_ALIGN_TARGET_SAMPLE_SIZE);
    }

    public void deInit() {
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        extensionLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        arm.setPower(0); // prevent locking
        extensionLift.setPower(0); // prevent locking
    }

    public void setRRDrivePose(Pose2d pose) {
        drive.updatePoseEstimate();
        drive.pose = pose;
    }
}
