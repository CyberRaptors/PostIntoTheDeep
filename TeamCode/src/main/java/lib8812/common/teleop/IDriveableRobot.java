package lib8812.common.teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public interface IDriveableRobot {
    DcMotor rightFront = null;
    DcMotor leftFront = null;
    DcMotor rightBack = null;
    DcMotor leftBack = null;

    void init(HardwareMap hardwareMap);
}
