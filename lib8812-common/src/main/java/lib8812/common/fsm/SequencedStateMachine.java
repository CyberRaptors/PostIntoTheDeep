package lib8812.common.fsm;

import java.util.ArrayList;

public final class SequencedStateMachine {
	int stateIdx = 0;
	State currState = null;
	final ArrayList<State> states;

	public SequencedStateMachine(ArrayList<State> sequencedStates) {
		if (sequencedStates.isEmpty()) throw new IllegalArgumentException("State machine list needs at least one state!");

		states = new ArrayList<>(sequencedStates);

		changeState(0);
	}

	public void changeState(State state) {
		if (!states.contains(state)) throw new IllegalArgumentException("Cannot change to state not specified in initial state list!");

		changeState(states.indexOf(state));
	}

	public void changeState(int i) {
		if (states.size() <= i || i < 0) throw new IllegalArgumentException(String.format("Invalid state index: %d", i));

		stateIdx = i;

		if (currState != null) currState.exit();

		currState = states.get(i);

		currState.enter();
	}

	public void nextState() {
		if (stateIdx == states.size()-1) changeState(0);
		else changeState(stateIdx+1);
	}

	public void update() {
		currState.maintain();
	}
}
