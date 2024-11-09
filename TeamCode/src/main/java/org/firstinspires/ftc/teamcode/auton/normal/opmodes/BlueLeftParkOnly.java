package org.firstinspires.ftc.teamcode.auton.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.normal.runners.BlueLeftParkOnlyRunner;

@Autonomous(name="Blue/Left/ParkOnly [*    ]", group="Blue")
public class BlueLeftParkOnly extends LinearOpMode {
	@Override
	public void runOpMode() {
		new BlueLeftParkOnlyRunner().run(this);
	}
}
