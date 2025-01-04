package lib8812.common.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import lib8812.common.robot.hardwarewrappers.ServoLikeMotor;

public class MotorSetPositionAction implements Action {
	final ServoLikeMotor motor;
	final int targetPos;
	final int window;
	final String ident;

	boolean initialized = false;

	public MotorSetPositionAction(ServoLikeMotor motor, int pos, int window, String ident) {
		this.motor = motor;
		this.window = window;
		this.ident = ident;
		targetPos = pos;
	}

	public MotorSetPositionAction(ServoLikeMotor motor, int pos) {
		this(motor, pos, 10);
	}

	public MotorSetPositionAction(ServoLikeMotor motor, int pos, String ident) {
		this(motor, pos, 10, ident);
	}

	public MotorSetPositionAction(ServoLikeMotor motor, int pos, int window) {
		this(motor, pos, window, "motor");
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

//		if (!motor.isBusy()) return false;
//
		return Math.abs(motor.getPosition()-targetPos) > window;

//		return motor.isBusy();
	}
}
