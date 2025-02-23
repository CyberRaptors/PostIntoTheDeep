package lib8812.common.fsm;

public abstract class State {
	public void enter() { }
	public void exit() { }

	abstract void maintain();
}
