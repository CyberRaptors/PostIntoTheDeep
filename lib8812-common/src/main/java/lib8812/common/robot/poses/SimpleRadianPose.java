package lib8812.common.robot.poses;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

public class SimpleRadianPose implements IRadianPose {
	public final double x;
	public final double y;
	public final double h;

	public SimpleRadianPose(double x, double y, double h) {
		this.x = x;
		this.y = y;
		this.h = h;
	}

	public static SimpleDegreePose fromRoadRunner(Pose2d roadRunnerPose) {
		return new SimpleDegreePose(roadRunnerPose.position.x, roadRunnerPose.position.y, roadRunnerPose.heading.real);
	}

	public static SimpleDegreePose fromSparkFunDegrees(SparkFunOTOS.Pose2D sparkFunDegrees) {
		return new SimpleDegreePose(sparkFunDegrees.x, sparkFunDegrees.y, Math.toRadians(sparkFunDegrees.h));
	}

	public static SimpleDegreePose fromSparkFunRadians(SparkFunOTOS.Pose2D sparkFunRadians) {
		return new SimpleDegreePose(sparkFunRadians.x, sparkFunRadians.y, sparkFunRadians.h);
	}

	public Pose2d toRoadRunner() {
		return new Pose2d(x, y, h);
	}

	public SparkFunOTOS.Pose2D toSparkFunDegrees() {
		return new SparkFunOTOS.Pose2D(x, y, Math.toDegrees(h));
	}

	public SparkFunOTOS.Pose2D toSparkFunRadians() {
		return new SparkFunOTOS.Pose2D(x, y, h);
	}

	public SimpleDegreePose toDegrees() {
		return new SimpleDegreePose(x, y, Math.toDegrees(h));
	}

	public SimpleRadianPose toRadians() {
		return this;
	}
}
