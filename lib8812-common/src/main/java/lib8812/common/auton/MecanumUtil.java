package lib8812.common.auton;

import lib8812.common.actions.ActionsUtil;
import lib8812.common.rr.MecanumDrive;

public class MecanumUtil extends ActionsUtil {
	final MecanumDrive drive;

	public MecanumUtil(MecanumDrive mecanumDrive) {
		drive = mecanumDrive;
	}

//	public Action splineToPose(Pose2d target) {
//		return drive.actionBuilder(target).tr
//	}
}
