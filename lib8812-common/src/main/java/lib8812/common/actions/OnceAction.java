package lib8812.common.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

public class OnceAction implements Action {
	public interface Predicate
	{
		boolean run();
	}

	final Predicate predicate;
	boolean started = false;
	final Action action;

	public OnceAction(Predicate predicate, Action then) {
		this.predicate = predicate;
		action = then;
	}

	@Override
	public boolean run(@NonNull TelemetryPacket telemetryPacket) {
		if (started) {
			return action.run(telemetryPacket);
		}
		else if (predicate.run()) {
			started = true;
			return run(telemetryPacket);
		}

		return true;
	}
}
