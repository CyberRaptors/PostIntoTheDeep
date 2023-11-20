package lib8812.common.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class ServoLikeMotor implements DcMotor {
    int position = 0;
    DcMotor inner;

    public int minPos;
    public int maxPos;

    public ServoLikeMotor(DcMotor innerMotor, int minPos, int maxPos)
    {
        inner = innerMotor;
        this.minPos = minPos;
        this.maxPos = maxPos;

        inner.setMode(RunMode.RUN_USING_ENCODER);
        inner.setMode(RunMode.STOP_AND_RESET_ENCODER);
        inner.setMode(RunMode.RUN_TO_POSITION);
    }

    public void setPosition(int pos) {
        if (inner.getMode() == RunMode.RUN_WITHOUT_ENCODER) {
            inner.setMode(RunMode.RUN_USING_ENCODER);
            inner.setMode(RunMode.STOP_AND_RESET_ENCODER);
            inner.setMode(RunMode.RUN_TO_POSITION);
        }

        position = Math.min(Math.max(pos, minPos), maxPos); // cap positions


        inner.setPower(1);
        inner.setTargetPosition(position);

        while (inner.isBusy());

        inner.setPower(0);
    }

    public int getPosition() {
        return position;
    }

    @Override
    public MotorConfigurationType getMotorType() {
        return inner.getMotorType();
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {
        inner.setMotorType(motorType);
    }

    @Override
    public DcMotorController getController() {
        return inner.getController();
    }

    @Override
    public int getPortNumber() {
        return inner.getPortNumber();
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        inner.setZeroPowerBehavior(zeroPowerBehavior);
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return inner.getZeroPowerBehavior();
    }

    @Override
    @Deprecated
    public void setPowerFloat() {
        inner.setPowerFloat();
    }

    @Override
    public boolean getPowerFloat() {
        return inner.getPowerFloat();
    }

    @Override
    public void setTargetPosition(int position) {
        inner.setTargetPosition(position);
    }

    @Override
    public int getTargetPosition() {
        return inner.getTargetPosition();
    }

    @Override
    public boolean isBusy() {
        return inner.isBusy();
    }

    @Override
    public int getCurrentPosition() {
        return inner.getCurrentPosition();
    }

    @Override
    public void setMode(RunMode mode) {
        inner.setMode(mode);
    }

    @Override
    public RunMode getMode() {
        return inner.getMode();
    }

    @Override
    public void setDirection(Direction direction) {
        inner.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return inner.getDirection();
    }

    @Override
    public void setPower(double power) {
        inner.setMode(RunMode.RUN_WITHOUT_ENCODER);

        inner.setPower(power);
    }

    @Override
    public double getPower() {
        return inner.getPower();
    }

    @Override
    public Manufacturer getManufacturer() {
        return inner.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return inner.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return inner.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return inner.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        inner.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        inner.close();
    }
}
