package lib8812.common.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import lib8812.common.robot.hardwarewrappers.ServoLikeMotor;

public class MotorSetPositionAction implements Action {
	final ServoLikeMotor motor;
	final int targetPos;

	boolean initialized = false;

	public MotorSetPositionAction(ServoLikeMotor motor, int pos) {
		this.motor = motor;
		targetPos = pos;
	}

	@Override
	public boolean run(@NonNull TelemetryPacket telemetryPacket) {
		if (!initialized) {
			motor.setPosition(targetPos);
			initialized = true;
		}

		telemetryPacket.put("target pos", targetPos);
		telemetryPacket.put("motor pos", motor.getPosition());

		return Math.abs(motor.getPosition()-targetPos) > 10;
	}
}
