package lib8812.common.robot.hardwarewrappers;

import com.qualcomm.robotcore.hardware.Servo;

public class BinaryClaw implements ICustomHardwareDevice {
    public LabeledPositionServo inner;

    public boolean isVirtualDevice() { return false; }

    public BinaryClaw(Servo innerServo, double openPos, double closedPos) {
        inner = new LabeledPositionServo(
                innerServo,
                new String[] { "open", "closed" },
                new Double[] { openPos, closedPos }
        );
    }

  public void open() {
        inner.setLabeledPosition("open");
  }

  public void close() {
        inner.setLabeledPosition("closed");
  }

  public String getStatus() {
        return inner.getPositionLabel();
  }

  public boolean isOpen() {
        return inner.getPositionLabel().equals("open");
  }

  public boolean isClosed() {
        return !isOpen();
  }

  public void toggle() {
        if (isOpen()) close();
        else open();
  }
}
