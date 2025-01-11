package lib8812.common.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import lib8812.common.robot.hardwarewrappers.ServoLikeMotor;

public class MotorSetPositionAction implements Action {
	final static long STALL_FAILSAFE_DELTA_MS = 100;

	final ServoLikeMotor motor;
	final int targetPos;
	final int window;
	final String ident;
	boolean stallFailSafe = false;

	int last = Integer.MIN_VALUE;
	long lastTime = -1;
	long failSafeDeltaTime = 0;

	boolean initialized = false;

	public MotorSetPositionAction(ServoLikeMotor motor, int pos, int window, String ident, boolean stallFailSafe) {
		this.motor = motor;
		this.window = window;
		this.ident = ident;
		this.stallFailSafe = stallFailSafe;
		targetPos = pos;
	}

	public MotorSetPositionAction(ServoLikeMotor motor, int pos, boolean stallFailSafe) {
		this(motor, pos, 10, stallFailSafe);
	}

	public MotorSetPositionAction(ServoLikeMotor motor, int pos, String ident, boolean stallFailSafe) {
		this(motor, pos, 10, ident, stallFailSafe);
	}

	public MotorSetPositionAction(ServoLikeMotor motor, int pos, int window, boolean stallFailSafe) {
		this(motor, pos, window, "motor", stallFailSafe);
	}

	public MotorSetPositionAction(ServoLikeMotor motor, int pos, int window, String ident) {
		this(motor, pos, window, ident, false);
	}


	public MotorSetPositionAction(ServoLikeMotor motor, int pos) {
		this(motor, pos, 10, false);
	}

	public MotorSetPositionAction(ServoLikeMotor motor, int pos, String ident) {
		this(motor, pos, 10, ident, false);
	}

	public MotorSetPositionAction(ServoLikeMotor motor, int pos, int window) {
		this(motor, pos, window, "motor", false);
	}

	@Override
	public boolean run(@NonNull TelemetryPacket telemetryPacket) {
		if (!initialized) {
			motor.setPosition(targetPos);
			initialized = true;
		}

		telemetryPacket.put("target pos", targetPos);
		telemetryPacket.put("motor pos", motor.getPosition());
		telemetryPacket.put("motor name", ident);

		long currTime = System.currentTimeMillis();

		if (last != Integer.MIN_VALUE && lastTime > 0) {
			if (last == motor.getPosition()) {
				failSafeDeltaTime+=(currTime-lastTime);

				if (failSafeDeltaTime >= STALL_FAILSAFE_DELTA_MS) return false; // combat lift slipping
			}
			else { failSafeDeltaTime = 0; }
		}

		last = motor.getPosition();
		lastTime = currTime;

		return Math.abs(last-targetPos) > window;

//		return motor.isBusy();
	}
}
