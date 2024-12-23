package lib8812.common.auton;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;

import lib8812.common.actions.ActionsUtil;
import lib8812.common.rr.MecanumDrive;

public class MecanumUtil extends ActionsUtil {
	final MecanumDrive drive;

	public MecanumUtil(MecanumDrive mecanumDrive) {
		drive = mecanumDrive;
	}

	public Action relocalize() {
		return new InstantAction(drive::updatePoseEstimate);
	}
}
