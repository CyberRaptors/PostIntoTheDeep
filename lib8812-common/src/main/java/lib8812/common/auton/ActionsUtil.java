package lib8812.common.auton;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.InstantFunction;

public class ActionsUtil {
	public InstantAction run(InstantFunction func) {
		return new InstantAction(func);
	}
}
