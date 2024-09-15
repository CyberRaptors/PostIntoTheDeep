package lib8812.common.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.ServoController;

public final class VirtualCRServo implements ICustomHardwareDevice, CRServo {
    public boolean isVirtualDevice() { return true; }

    public ServoController getController() {
        return null;
    }

    public int getPortNumber() {
        return 0;
    }

    public void setDirection(DcMotorSimple.Direction direction) {

    }

    public DcMotorSimple.Direction getDirection() {
        return null;
    }

    public void setPower(double power) {

    }

    public double getPower() {
        return 0;
    }

    public HardwareDevice.Manufacturer getManufacturer() {
        return null;
    }

    public String getDeviceName() {
        return null;
    }

    public String getConnectionInfo() {
        return null;
    }

    public int getVersion() {
        return 0;
    }

    public void resetDeviceConfigurationForOpMode() {

    }

    public void close() {

    }
}
