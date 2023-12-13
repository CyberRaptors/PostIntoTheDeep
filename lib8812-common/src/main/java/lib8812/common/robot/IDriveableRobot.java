package lib8812.common.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import lib8812.common.robot.uapi.IPixelManager;

public abstract class IDriveableRobot {
    public DcMotor rightFront = null;
    public DcMotor leftFront = null;
    public DcMotor rightBack = null;
    public DcMotor leftBack = null;
    public IPixelManager pixelManager;

    public abstract void init(HardwareMap hardwareMap);

    protected static <THardwareDevice> THardwareDevice loadDevice(HardwareMap hardwareMap, Class<? extends THardwareDevice> cls, String name) {
        if (cls.equals(VirtualMotor.class)) return (THardwareDevice) new VirtualMotor();
        if (cls.equals(VirtualServo.class)) return (THardwareDevice) new VirtualServo();
        if (cls.equals(VirtualCRServo.class)) return (THardwareDevice) new VirtualCRServo();


        return hardwareMap.get(cls, name);
    }

}
