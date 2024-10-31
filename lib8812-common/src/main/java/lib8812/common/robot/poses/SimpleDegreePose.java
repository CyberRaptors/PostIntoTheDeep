package lib8812.common.robot.poses;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

public class SimpleDegreePose extends IDegreePose {
	public SimpleDegreePose(double x, double y, double h) {
		this.x = x;
		this.y = y;
		this.h = h;
	}

	public static SimpleDegreePose fromRoadRunner(Pose2d roadRunnerPose) {
		return new SimpleDegreePose(roadRunnerPose.position.x, roadRunnerPose.position.y, Math.toDegrees(roadRunnerPose.heading.real));
	}

	public static SimpleDegreePose fromSparkFunDegrees(SparkFunOTOS.Pose2D sparkFunDegrees) {
		return new SimpleDegreePose(sparkFunDegrees.x, sparkFunDegrees.y, sparkFunDegrees.h);
	}

	public static SimpleDegreePose fromSparkFunRadians(SparkFunOTOS.Pose2D sparkFunRadians) {
		return new SimpleDegreePose(sparkFunRadians.x, sparkFunRadians.y, Math.toRadians(sparkFunRadians.h));
	}

	public Pose2d toRoadRunner() {
		return new Pose2d(x, y, Math.toRadians(h));
	}

	public SparkFunOTOS.Pose2D toSparkFunDegrees() {
		return this;
	}

	public SparkFunOTOS.Pose2D toSparkFunRadians() {
		return new SparkFunOTOS.Pose2D(x, y, Math.toRadians(h));
	}

	public SimpleRadianPose toRadians() {
		return new SimpleRadianPose(x, y, Math.toRadians(h));
	}

	public SimpleDegreePose toDegrees() {
		return this;
	}
}
