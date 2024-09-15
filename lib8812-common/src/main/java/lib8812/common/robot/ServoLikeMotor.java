package lib8812.common.robot;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ServoLikeMotor implements DcMotor, ICustomHardwareDevice {
    int targetPosition = 0;
    CompletableFuture<Boolean> currentAntiStressWatcher;
    DcMotorEx inner;
    public int minPos;
    public int maxPos;
    public boolean antiStressAutomatic = false;

    public boolean isVirtualDevice() { return false; }

    public ServoLikeMotor(DcMotorEx innerMotor, int minPos, int maxPos)
    {
        inner = innerMotor;
        this.minPos = minPos;
        this.maxPos = maxPos;

        inner.setMode(RunMode.RUN_USING_ENCODER);
        inner.setMode(RunMode.STOP_AND_RESET_ENCODER);
        inner.setMode(RunMode.RUN_TO_POSITION);
    }

    public void enableAlgorithmAutomatic(String name) {
        if (name.equals("anti-stress")) {
            antiStressAutomatic = true;
        }

        else throw new IllegalArgumentException(String.format("Algorithm '%s' unknown.", name));
    }

    public void disableAlgorithmAutomatic(String name) {
        if (name.equals("anti-stress")) {
            antiStressAutomatic = false;
        }

        else throw new IllegalArgumentException(String.format("Algorithm '%s' unknown.", name));
    }

    public void resetEncoder() {
        if (inner.getMode() == RunMode.RUN_WITHOUT_ENCODER) {
            inner.setMode(RunMode.RUN_USING_ENCODER);
            inner.setMode(RunMode.STOP_AND_RESET_ENCODER);
            inner.setMode(RunMode.RUN_TO_POSITION);
        } else {
            inner.setMode(RunMode.STOP_AND_RESET_ENCODER);
            inner.setMode(RunMode.RUN_TO_POSITION);
        }
    }

    public void reverse() {
        if (inner.getDirection() == Direction.FORWARD) inner.setDirection(Direction.REVERSE);
        else inner.setDirection(Direction.FORWARD);
    }

    public void setPosition(int pos, boolean startAutoAntiStress) {
        targetPosition = Math.min(Math.max(pos, minPos), maxPos); // cap positions

        if (inner.getMode() == RunMode.RUN_WITHOUT_ENCODER) {
            inner.setMode(RunMode.RUN_USING_ENCODER);
            inner.setMode(RunMode.STOP_AND_RESET_ENCODER);
            inner.setMode(RunMode.RUN_TO_POSITION);
        }

        inner.setTargetPosition(targetPosition);

        inner.setPower(1);

        if (startAutoAntiStress) {
            currentAntiStressWatcher = CompletableFuture.supplyAsync(this::relieveStress);
        }
    }

    public void setPosition(int pos) {
        setPosition(pos, antiStressAutomatic && (currentAntiStressWatcher == null || currentAntiStressWatcher.isDone()) && (Math.abs(inner.getCurrentPosition()- targetPosition) > 15)); // the Math.abs... bit makes sure that the anti-stress only runs when a gap between desired and real position accumulates
    }

    public int getPosition() {
        return getCurrentPosition();
    }

    public boolean relieveStress() {
        boolean relieved = false;

        try { TimeUnit.MILLISECONDS.sleep(15); }
        catch (InterruptedException ignored) {}

        while (inner.isBusy()) {
            int ticks = inner.getCurrentPosition();
            if ((ticks > 200) && (Math.abs(inner.getVelocity()) < 12) && (Math.abs(targetPosition -ticks) > 1)) { // low speed and more than 5 ticks away from target (when target is greater than current pos)
                setPosition(targetPosition -20, false);
                relieved = true;
            }
        }

        return relieved;
    }

    public boolean waitForPosition() {
        return waitForPosition(antiStressAutomatic);
    }

    public boolean waitForPosition(boolean runAntiStress) {
        if (runAntiStress) {
            if (currentAntiStressWatcher != null) {
                currentAntiStressWatcher.cancel(true);
            }

            currentAntiStressWatcher = CompletableFuture.supplyAsync(() -> false);

            boolean relieved = relieveStress();

            currentAntiStressWatcher.cancel(true);
            currentAntiStressWatcher = null;

            return relieved;
        }

        while (inner.isBusy());

        return false;
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
    public void setDirection(DcMotor.Direction direction) {
        inner.setDirection(direction);
    }

    @Override
    public DcMotorSimple.Direction getDirection() {
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
