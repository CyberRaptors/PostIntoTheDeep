package lib8812.common.robot;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

public final class VirtualServo implements IVirtualHardwareDevice {
    public boolean isVirtualDevice() { return true; }

    public ServoController getController() {
        return null;
    }

    public int getPortNumber() {
        return 0;
    }

    public void setDirection(Servo.Direction direction) {

    }

    public Servo.Direction getDirection() {
        return null;
    }

    public void setPosition(double position) {

    }

    public double getPosition() {
        return 0;
    }

    public void scaleRange(double min, double max) {

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
