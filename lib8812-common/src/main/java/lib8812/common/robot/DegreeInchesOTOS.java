package lib8812.common.robot;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import lib8812.common.robot.poses.SimpleDegreePose;

public class DegreeInchesOTOS implements ICustomHardwareDevice {
	public static class Configuration {
		SparkFunOTOS.Pose2D offset;
		SparkFunOTOS.Pose2D startingPoint;
		double linearMultiplier;
		double angularMultiplier;

		public Configuration withOffset(SimpleDegreePose offset) {
			this.offset = offset.toSparkFunDegrees();

			return this;
		}

		public Configuration withOffset(double x, double y, double h) {
			this.offset = new SparkFunOTOS.Pose2D(x, y, h);

			return this;
		}

		public Configuration withStartingPoint(SimpleDegreePose startingPoint) {
			this.startingPoint = startingPoint.toSparkFunDegrees();

			return this;
		}

		public Configuration withStartingPoint(double x, double y, double h) {
			this.startingPoint = new SparkFunOTOS.Pose2D(x, y, h);

			return this;
		}

		public Configuration withLinearMultiplier(double linearMultiplier) {
			this.linearMultiplier = linearMultiplier;

			return this;
		}

		public Configuration withAngularMultiplier(double angularMultiplier) {
			this.angularMultiplier = angularMultiplier;

			return this;
		}
	}

	public final SparkFunOTOS inner;

	public DegreeInchesOTOS(SparkFunOTOS otos, Configuration config) {
		otos.setLinearUnit(DistanceUnit.INCH);
		otos.setAngularUnit(AngleUnit.DEGREES);

		otos.setOffset(config.offset);

		otos.setLinearScalar(1);
		otos.setAngularScalar(1);

		otos.setLinearScalar(config.linearMultiplier);
		otos.setAngularScalar(config.angularMultiplier);

		otos.calibrateImu();
		otos.resetTracking();

		otos.setPosition(config.startingPoint);

		inner = otos;
	}

	public boolean isVirtualDevice() {
		return false;
	}

	public SimpleDegreePose getPosition() {
		return SimpleDegreePose.fromSparkFunDegrees(inner.getPosition());
	}

	public SimpleDegreePose getVelocity() {
		return SimpleDegreePose.fromSparkFunDegrees(inner.getVelocity());
	}

	public void setPosition(SimpleDegreePose pos) {
		inner.setPosition(pos.toSparkFunDegrees());
	}

	static String angleUnitToString(AngleUnit angularUnit) {
		if (angularUnit == AngleUnit.DEGREES) return "deg";

		return "rad";
	}

	public void emitPositionTelemetryTo(Telemetry telemetry) {
		SimpleDegreePose pos = getPosition();
		DistanceUnit linearUnit = inner.getLinearUnit();
		AngleUnit angularUnit = inner.getAngularUnit();

		telemetry.addData(
				"Robot Position",
				"x (%.2f%s), y (%.2f%s), h (%.2f%s)",
				pos.x, linearUnit, pos.y, linearUnit, pos.h, angleUnitToString(angularUnit)
		);
	}
}
