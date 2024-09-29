package lib8812.common.robot.hardwarewrappers;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class MockMotor implements DcMotorEx, ICustomHardwareDevice {
	public interface SingleReturn<T> {
		T run();
	}

	public interface SingleArgument<T> {
		void run(T arg);
	}

	public interface DoubleArgument<T1, T2> {
		void run(T1 arg1, T2 arg2);
	}

	public interface SingleReturnSingleArg<T, R> {
		R run(T arg);
	}

	public interface FourDouble {
		void run(double a, double b, double c, double d);
	}

	public Runnable setMotorEnable = () -> {};
	public Runnable setMotorDisable = () -> {};
	public SingleReturn<Boolean> isMotorEnabled = () -> false;
	public SingleArgument<Double> setVelocity = (x) -> {};
	public DoubleArgument<Double, AngleUnit> setVelocityA = (x, y) -> {};
	public SingleReturn<Double> getVelocity = () -> 0d;
	public SingleReturnSingleArg<AngleUnit, Double> getVelocityA = (x) -> 0d;
	public DoubleArgument<RunMode, PIDCoefficients> setPIDCoefficients = (x, y) -> {};
	public DoubleArgument<RunMode, PIDFCoefficients> setPIDFCoefficients = (x, y) -> {};
	public FourDouble setVelocityPIDFCoefficients = (p, i, d, f) -> {};
	public SingleArgument<Double> setPositionPIDFCoefficients = (x) -> {};
	public SingleReturnSingleArg<RunMode, PIDCoefficients> getPIDCoefficients = (x) -> null;
	public SingleReturnSingleArg<RunMode, PIDFCoefficients> getPIDFCoefficients = (x) -> null;
	public SingleArgument<Integer> setTargetPositionTolerance = (x) -> {};
	public SingleReturn<Integer> getTargetPositionTolerance = () -> 0;
	public SingleReturnSingleArg<CurrentUnit, Double> getCurrent = (x) -> 0d;
	public SingleReturnSingleArg<CurrentUnit, Double> getCurrentAlert = (x) -> 0d;
	public DoubleArgument<Double, CurrentUnit> setCurrentAlert = (x, y) -> {};
	public SingleReturn<Boolean> isOverCurrent = () -> false;
	public SingleReturn<MotorConfigurationType> getMotorType = () -> null;
	public SingleArgument<MotorConfigurationType> setMotorType = (x) -> {};
	public SingleReturn<DcMotorController> getController = () -> null;
	public SingleReturn<Integer> getPortNumber = () -> 0;
	public SingleReturn<ZeroPowerBehavior> getZeroPowerBehavior = () -> null;
	public Runnable setPowerFloat = () -> {};
	public SingleReturn<Boolean> getPowerFloat = () -> false;
	public SingleArgument<Integer> setTargetPosition = (x) -> {};
	public SingleReturn<Integer> getTargetPosition = () -> 0;
	public SingleReturn<Boolean> isBusy = () -> false;
	public SingleReturn<Integer> getCurrentPosition = () -> 0;
	public SingleArgument<RunMode> setMode = (x) -> {};
	public SingleReturn<RunMode> getMode = () -> null;
	public SingleArgument<Direction> setDirection = (x) -> {};
	public SingleReturn<Direction> getDirection = () -> null;
	public SingleArgument<Double> setPower = (x) -> {};
	public SingleReturn<Double> getPower = () -> 0d;
	public SingleReturn<Manufacturer> getManufacturer = () -> null;
	public SingleReturn<String> getDeviceName = () -> "";
	public SingleReturn<String> getConnectionInfo = () -> "";
	public SingleReturn<Integer> getVersion = () -> 0;
	public Runnable resetDeviceConfigurationForOpMode = () -> {};
	public Runnable close = () -> {};


	@Override
	public void setMotorEnable() {
		setMotorEnable.run();
	}

	@Override
	public void setMotorDisable() {
		setMotorDisable.run();
	}

	@Override
	public boolean isMotorEnabled() {
		return isMotorEnabled.run();
	}

	@Override
	public void setVelocity(double angularRate) {
		setVelocity.run(angularRate);
	}

	@Override
	public void setVelocity(double angularRate, AngleUnit unit) {
		setVelocityA.run(angularRate, unit);
	}

	@Override
	public double getVelocity() {
		return getVelocity.run();
	}

	@Override
	public double getVelocity(AngleUnit unit) {
		return getVelocityA.run(unit);
	}

	@Override
	public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {
		setPIDCoefficients.run(mode, pidCoefficients);
	}

	@Override
	public void setPIDFCoefficients(RunMode mode, PIDFCoefficients pidfCoefficients) throws UnsupportedOperationException {
		setPIDFCoefficients.run(mode, pidfCoefficients);
	}

	@Override
	public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {
		setVelocityPIDFCoefficients.run(p, i, d, f);
	}

	@Override
	public void setPositionPIDFCoefficients(double p) {
		setPositionPIDFCoefficients.run(p);
	}

	@Override
	public PIDCoefficients getPIDCoefficients(RunMode mode) {
		return getPIDCoefficients.run(mode);
	}

	@Override
	public PIDFCoefficients getPIDFCoefficients(RunMode mode) {
		return getPIDFCoefficients.run(mode);
	}

	@Override
	public void setTargetPositionTolerance(int tolerance) {
		setTargetPositionTolerance.run(tolerance);
	}

	@Override
	public int getTargetPositionTolerance() {
		return getTargetPositionTolerance.run();
	}

	@Override
	public double getCurrent(CurrentUnit unit) {
		return getCurrent.run(unit);
	}

	@Override
	public double getCurrentAlert(CurrentUnit unit) {
		return getCurrentAlert.run(unit);
	}

	@Override
	public void setCurrentAlert(double current, CurrentUnit unit) {
		setCurrentAlert.run(current, unit);
	}

	@Override
	public boolean isOverCurrent() {
		return isOverCurrent.run();
	}

	@Override
	public MotorConfigurationType getMotorType() {
		return getMotorType.run();
	}

	@Override
	public void setMotorType(MotorConfigurationType motorType) {
		setMotorType.run(motorType);
	}

	@Override
	public DcMotorController getController() {
		return getController.run();
	}

	@Override
	public int getPortNumber() {
		return getPortNumber.run();
	}

	@Override
	public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
		setZeroPowerBehavior(zeroPowerBehavior);
	}

	@Override
	public ZeroPowerBehavior getZeroPowerBehavior() {
		return getZeroPowerBehavior.run();
	}

	@Override
	public void setPowerFloat() {
		setPowerFloat.run();
	}

	@Override
	public boolean getPowerFloat() {
		return getPowerFloat.run();
	}

	@Override
	public void setTargetPosition(int position) {
		setTargetPosition.run(position);
	}

	@Override
	public int getTargetPosition() {
		return getTargetPosition.run();
	}

	@Override
	public boolean isBusy() {
		return isBusy.run();
	}

	@Override
	public int getCurrentPosition() {
		return getCurrentPosition.run();
	}

	@Override
	public void setMode(RunMode mode) {
		setMode.run(mode);
	}

	@Override
	public RunMode getMode() {
		return getMode.run();
	}

	@Override
	public void setDirection(Direction direction) {
		setDirection.run(direction);
	}

	@Override
	public Direction getDirection() {
		return getDirection.run();
	}

	@Override
	public void setPower(double power) {
		setPower.run(power);
	}

	@Override
	public double getPower() {
		return getPower.run();
	}

	@Override
	public Manufacturer getManufacturer() {
		return getManufacturer.run();
	}

	@Override
	public String getDeviceName() {
		return getDeviceName.run();
	}

	@Override
	public String getConnectionInfo() {
		return getConnectionInfo.run();
	}

	@Override
	public int getVersion() {
		return getVersion.run();
	}

	@Override
	public void resetDeviceConfigurationForOpMode() {
		resetDeviceConfigurationForOpMode.run();
	}

	@Override
	public void close() {
		close.run();
	}

	@Override
	public boolean isVirtualDevice() {
		return true;
	}
}
