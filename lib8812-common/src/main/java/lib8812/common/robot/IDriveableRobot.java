package lib8812.common.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import lib8812.common.robot.hardwarewrappers.VirtualCRServo;
import lib8812.common.robot.hardwarewrappers.VirtualMotor;
import lib8812.common.robot.hardwarewrappers.VirtualServo;

public abstract class IDriveableRobot {
    public DcMotor rightFront;
    public DcMotor leftFront;
    public DcMotor rightBack;
    public DcMotor leftBack;

    public abstract void init(HardwareMap hardwareMap);

    protected static <THardwareDevice> THardwareDevice loadDevice(HardwareMap hardwareMap, Class<THardwareDevice> cls, String name) {
        if (cls.equals(VirtualMotor.class)) return (THardwareDevice) new VirtualMotor();
        if (cls.equals(VirtualServo.class)) return (THardwareDevice) new VirtualServo();
        if (cls.equals(VirtualCRServo.class)) return (THardwareDevice) new VirtualCRServo();

        return hardwareMap.get(cls, name);
    }

}
