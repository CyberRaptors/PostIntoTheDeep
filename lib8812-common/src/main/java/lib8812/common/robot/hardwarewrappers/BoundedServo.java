package lib8812.common.robot.hardwarewrappers;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

public class BoundedServo implements ICustomHardwareDevice, Servo {
	final Servo inner;

	public double minPos = 0;
	public double maxPos = 1;
	public double increment;

	public BoundedServo(Servo servo) {
		inner = servo;
	}

	public BoundedServo attachBounds(double min, double max) {
		minPos = min;
		maxPos = max;

		return this;
	}

	public BoundedServo attachIncrement(double inc) {
		increment = inc;

		return this;
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
		inner.setPosition(
				Math.min(
						Math.max(position, minPos),
						maxPos
				)
		);
	}

	@Override
	public double getPosition() {
		return inner.getPosition();
	}

	@Override
	public void scaleRange(double min, double max) {
		throw new UnsupportedOperationException("Cannot scale range on bounded servo");
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

	@Override
	public boolean isVirtualDevice() {
		return false;
	}
}
