package org.firstinspires.ftc.teamcode.raptor.robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

public class RaptorRobot {


    public DcMotor leftBack;
    public DcMotor rightBack;
    public DcMotor leftFront;
    public DcMotor rightFront;
    private BNO055IMU classedIMU;
    public IMU imu;

    public void init(HardwareMap hardwareMap) {
		//DcMotors
		rightFront  = hardwareMap.get(DcMotor.class, "rightFront");
		leftFront = hardwareMap.get(DcMotor.class, "leftFront");
		rightBack = hardwareMap.get(DcMotor.class, "rightBack");
		leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        classedIMU = hardwareMap.get(BNO055IMU.class, "imu");

        classedIMU.initialize(new BNO055IMU.Parameters()); // TODO: change params later?

        imu = (IMU) classedIMU;
	}
}
