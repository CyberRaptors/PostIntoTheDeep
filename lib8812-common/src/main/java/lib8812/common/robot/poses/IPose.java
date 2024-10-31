package lib8812.common.robot.poses;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

public abstract class IPose extends SparkFunOTOS.Pose2D {
	abstract Pose2d toRoadRunner();
	abstract SparkFunOTOS.Pose2D toSparkFunDegrees();
	abstract SparkFunOTOS.Pose2D toSparkFunRadians();
	abstract IDegreePose toDegrees();
	abstract IRadianPose toRadians();
}
