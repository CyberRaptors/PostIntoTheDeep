package lib8812.common.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import lib8812.common.util.ZeroArgPredicate;

public class InitAndPredicateAction implements Action {
	final Runnable init;
	final ZeroArgPredicate isComplete;
	boolean initialized = false;

	public InitAndPredicateAction(Runnable initF, ZeroArgPredicate actionIsComplete) {
		init = initF;
		isComplete = actionIsComplete;
	}

	@Override
	public boolean run(@NonNull TelemetryPacket telemetryPacket) {
		if (!initialized) {
			init.run();
			initialized = true;
		}

		return !isComplete.run();
	}
}
