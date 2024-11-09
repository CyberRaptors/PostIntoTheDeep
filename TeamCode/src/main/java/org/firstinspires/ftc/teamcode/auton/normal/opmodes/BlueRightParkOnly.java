package org.firstinspires.ftc.teamcode.auton.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.normal.runners.BlueRightParkOnlyRunner;

@Autonomous(name="Blue/Right/ParkOnly [*    ]", group="Blue")
public class BlueRightParkOnly extends LinearOpMode {
	@Override
	public void runOpMode() {
		new BlueRightParkOnlyRunner().run(this);
	}
}
