package lib8812.common.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.InstantFunction;
import com.acmerobotics.roadrunner.SequentialAction;

import java.util.ArrayList;

public class RRActionsDelegator {
	final FtcDashboard dash = FtcDashboard.getInstance();
	final ArrayList<Action> actions = new ArrayList<>();

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

		ArrayList<Action> done = new ArrayList<>();

		for (Action action : actions) {
			action.preview(packet.fieldOverlay());

			if (!action.run(packet)) {
				done.add(action);
			}
		}


		actions.removeAll(done);

		dash.sendTelemetryPacket(packet);
	}
}
