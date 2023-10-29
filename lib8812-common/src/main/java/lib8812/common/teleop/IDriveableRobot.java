package lib8812.common.teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import lib8812.common.robot.VirtualCRServo;
import lib8812.common.robot.VirtualMotor;
import lib8812.common.robot.VirtualServo;

public abstract class IDriveableRobot {
    DcMotor rightFront = null;
    DcMotor leftFront = null;
    DcMotor rightBack = null;
    DcMotor leftBack = null;

    public abstract void init(HardwareMap hardwareMap);

    protected <THardwareDevice> THardwareDevice loadDevice(HardwareMap hardwareMap, Class<? extends THardwareDevice> cls, String name) {
        if (cls.equals(VirtualMotor.class)) return (THardwareDevice) new VirtualMotor();
        if (cls.equals(VirtualServo.class)) return (THardwareDevice) new VirtualServo();
        if (cls.equals(VirtualCRServo.class)) return (THardwareDevice) new VirtualCRServo();


        return hardwareMap.get(cls, name);
    }

}
