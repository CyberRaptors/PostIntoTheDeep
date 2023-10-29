package lib8812.common.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public final class VirtualMotor implements IVirtualHardwareDevice {
    public boolean isVirtualDevice() { return true; }

    public MotorConfigurationType getMotorType() {
        return null;
    }

    public void setMotorType(MotorConfigurationType motorType) {

    }

    public DcMotorController getController() {
        return null;
    }

    public int getPortNumber() {
        return 0;
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {

    }

    public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
        return null;
    }

    public void setPowerFloat() {

    }

    public boolean getPowerFloat() {
        return false;
    }

    public void setTargetPosition(int position) {

    }

    public int getTargetPosition() {
        return 0;
    }

    public boolean isBusy() {
        return false;
    }

    public int getCurrentPosition() {
        return 0;
    }

    public void setMode(DcMotor.RunMode mode) {

    }

    public DcMotor.RunMode getMode() {
        return null;
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
