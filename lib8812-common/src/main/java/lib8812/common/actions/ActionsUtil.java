package lib8812.common.actions;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.InstantFunction;

public class ActionsUtil {
	public static InstantAction run(InstantFunction func) {
		return new InstantAction(func);
	}

	public static InstantAction debugException(String message) {
		return new InstantAction(() -> {
			throw new RuntimeException(message);
		});
	}

	public static InstantAction debugException() { return debugException("reached"); }
}
