package lib8812.common.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;

public class ServoLikeMotor implements DcMotor {
    int position = 0;
    boolean antiStressAutomatic = false;
    CompletableFuture<Boolean> currentAntiStressWatcher;
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

    public void enableAutoAntiStress() { antiStressAutomatic = true; }

    public void setPosition(int pos, boolean startAutoAntiStress) {
//        waitForPosition();

        position = Math.min(Math.max(pos, minPos), maxPos); // cap positions

        if (inner.getMode() == RunMode.RUN_WITHOUT_ENCODER) {
            inner.setMode(RunMode.RUN_USING_ENCODER);
            inner.setMode(RunMode.STOP_AND_RESET_ENCODER);
            inner.setMode(RunMode.RUN_TO_POSITION);
        }

        inner.setTargetPosition(position);

        inner.setPower(1);

        if (startAutoAntiStress && antiStressAutomatic) currentAntiStressWatcher = CompletableFuture.supplyAsync(this::relieveStress);
    }

    public void setPosition(int pos) {
        setPosition(pos, currentAntiStressWatcher == null || currentAntiStressWatcher.isDone()); // only start new auto anti stress if there is no old one or if the old one is done
    }

    public int getPosition() {
        return position;
    }

    public boolean relieveStress() {
        boolean relieved = false;
        int counter = 0;
        Stack<Integer> prevPos = new Stack<>();

        prevPos.push(inner.getCurrentPosition());

        while (inner.isBusy()) {
            if (counter % 100 == 0) { // anti-stress failsafe
                try {
                    if ((inner.getCurrentPosition()-prevPos.peek()) < 2) // if little to no change within 100 iterations, stop the movement
                    {
                        inner.setTargetPosition(prevPos.peek()); // don't break out of the loop, if the motor is still busy that means there is still stress
                        position = prevPos.pop(); // we pop instead of peek here so that if the motor is still being stress it has another fallback pos
                        relieved = true;
                    }
                    else prevPos.push(inner.getCurrentPosition());
                }
                catch (EmptyStackException e) {
                    // when setting the new pos, don't start another async job
                    setPosition(position-75, false); // if the arm was already stressed and there is no fallback position, then generate a fallback position 75 ticks back (since this is in a loop it will run incrementally until stress is relieved)
                }
            }

            counter++;
        }

        return relieved;
    }

    public void waitForPosition() {
        if (antiStressAutomatic) relieveStress();
        else while (inner.isBusy());
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
