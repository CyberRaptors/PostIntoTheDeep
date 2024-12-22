package lib8812.common.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.InstantFunction;
import com.acmerobotics.roadrunner.SequentialAction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RRActionsDelegator {
	final FtcDashboard dash = FtcDashboard.getInstance();
	List<Action> actions = new ArrayList<>();

	public void schedule(Action... actionsList) {
		actions.add(
				new SequentialAction(actionsList)
		);
	}

	public void schedule(InstantFunction... funcs) {
		ArrayList<InstantAction> instantActions = new ArrayList<>(funcs.length);

		for (InstantFunction func : funcs) {
			instantActions.add(new InstantAction(func));
		}

		actions.add(new SequentialAction(instantActions));
	}

	public void clear() { actions.clear(); }

	public void execute() {
		TelemetryPacket packet = new TelemetryPacket();

		actions = actions
				.stream()
				.filter(action -> action.run(packet))
				.collect(Collectors.toList());


		dash.sendTelemetryPacket(packet);
	}

	public int count() { return actions.size(); }
}
