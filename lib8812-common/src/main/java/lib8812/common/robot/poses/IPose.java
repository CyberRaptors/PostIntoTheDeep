package lib8812.common.robot.poses;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

public interface IPose {
	Pose2d toRoadRunner();
	SparkFunOTOS.Pose2D toSparkFunDegrees();
	SparkFunOTOS.Pose2D toSparkFunRadians();
	IDegreePose toDegrees();
	IRadianPose toRadians();
}
