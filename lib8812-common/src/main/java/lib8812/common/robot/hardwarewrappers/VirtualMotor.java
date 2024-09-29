package lib8812.common.robot.hardwarewrappers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public final class VirtualMotor implements ICustomHardwareDevice, DcMotorEx {
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

    @Override
    public void setMotorEnable() {

    }

    @Override
    public void setMotorDisable() {

    }

    @Override
    public boolean isMotorEnabled() {
        return false;
    }

    @Override
    public void setVelocity(double angularRate) {

    }

    @Override
    public void setVelocity(double angularRate, AngleUnit unit) {

    }

    @Override
    public double getVelocity() {
        return 0;
    }

    @Override
    public double getVelocity(AngleUnit unit) {
        return 0;
    }

    @Override
    public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {

    }

    @Override
    public void setPIDFCoefficients(RunMode mode, PIDFCoefficients pidfCoefficients) throws UnsupportedOperationException {

    }

    @Override
    public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {

    }

    @Override
    public void setPositionPIDFCoefficients(double p) {

    }

    @Override
    public PIDCoefficients getPIDCoefficients(RunMode mode) {
        return null;
    }

    @Override
    public PIDFCoefficients getPIDFCoefficients(RunMode mode) {
        return null;
    }

    @Override
    public void setTargetPositionTolerance(int tolerance) {

    }

    @Override
    public int getTargetPositionTolerance() {
        return 0;
    }

    @Override
    public double getCurrent(CurrentUnit unit) {
        return 0;
    }

    @Override
    public double getCurrentAlert(CurrentUnit unit) {
        return 0;
    }

    @Override
    public void setCurrentAlert(double current, CurrentUnit unit) {

    }

    @Override
    public boolean isOverCurrent() {
        return false;
    }
}
