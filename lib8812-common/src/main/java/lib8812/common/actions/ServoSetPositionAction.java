package lib8812.common.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoSetPositionAction implements Action {
	final Servo servo;
	final double targetPos;

	boolean initialized = false;

	public ServoSetPositionAction(Servo servo, double pos) {
		this.servo = servo;
		targetPos = pos;
	}

	@Override
	public boolean run(@NonNull TelemetryPacket telemetryPacket) {
		if (!initialized) {
			servo.setPosition(targetPos);
			initialized = true;
		}

		return servo.getPosition() != targetPos;
	}
}
