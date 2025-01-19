package lib8812.common.robot.hardwarewrappers;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import lib8812.common.robot.poses.SimpleRadianPose;

public class LimelightManager implements ICustomHardwareDevice {
	public final Limelight3A inner;
	final SimpleRadianPose cameraOffset;
	final Limelight3A limelight; // purely a name alias for convenience
	int currentPipeline = 0;

	public LimelightManager(Limelight3A limelight, SimpleRadianPose centralOffset) {
		this.limelight = limelight;
		cameraOffset = centralOffset;
		inner = limelight;
	}

	public void init() {
		limelight.pipelineSwitch(currentPipeline);
		limelight.start();
	}

	public void pipelineSwitch(int pipeline) {
		currentPipeline = pipeline;
		limelight.pipelineSwitch(pipeline);
	}

	public LLResult getLatestResultEnsure() {
		while (true)
		{
			LLResult res = limelight.getLatestResult();

			if (res != null && res.isValid()) return res;
		}
	}

	public SimpleRadianPose getLatestPose() {
		LLResult res = getLatestResultEnsure();

		Pose3D pose = res.getBotpose();
		Position pos = pose.getPosition();
		YawPitchRollAngles orientation = pose.getOrientation();

		double heading = orientation.getYaw(AngleUnit.RADIANS);

		return new SimpleRadianPose( // NOTE: cameraOffset was removed from calculations for now because it may be added in the Limelight management software
				pos.x,
				pos.y,
				heading
		);
	}

	@Override
	public boolean isVirtualDevice() {
		return false;
	}
}
