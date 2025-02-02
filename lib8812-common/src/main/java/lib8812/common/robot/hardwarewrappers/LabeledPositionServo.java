package lib8812.common.robot.hardwarewrappers;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import java.util.ArrayList;
import java.util.Arrays;

public class LabeledPositionServo implements Servo, ICustomHardwareDevice {
    final Servo inner;
    ArrayList<String> labels;
    ArrayList<Double> positions;

    public boolean isVirtualDevice() { return false; }

    public LabeledPositionServo(Servo innerServo, String[] labels, Double[] positions) {
        inner = innerServo;

        assert labels.length == positions.length;

        this.labels = new ArrayList<>(Arrays.asList(labels));
        this.positions = new ArrayList<>(Arrays.asList(positions));

    }

    public void setLabeledPosition(String label) {
        try { setPosition(positions.get(labels.indexOf(label))); }
        catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(label);
        }
    }

    public String getPositionLabel() {
        try { return labels.get(positions.indexOf(getPosition())); }
        catch (IndexOutOfBoundsException e) {
            double pos = getPosition();

            double leastDiff = 1;
            int leastDiffIdx = -1;

            for (int i = 0; i < positions.size(); i++) {
                double diff = Math.abs(pos-positions.get(i));

                if (diff < leastDiff) {
                    leastDiff = diff;
                    leastDiffIdx = i;
                }
            }

            return labels.get(leastDiffIdx);
        }
    }

    public void addPositionLabel(String label, double pos) {
        labels.add(label);
        positions.add(pos);
    }

    @Override
    public ServoController getController() {
        return inner.getController();
    }

    @Override
    public int getPortNumber() {
        return inner.getPortNumber();
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
    public void setPosition(double position) {
        inner.setPosition(position);
    }

    @Override
    public double getPosition() {
        return inner.getPosition();
    }

    @Override
    public void scaleRange(double min, double max) {
        inner.scaleRange(min, max);
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
