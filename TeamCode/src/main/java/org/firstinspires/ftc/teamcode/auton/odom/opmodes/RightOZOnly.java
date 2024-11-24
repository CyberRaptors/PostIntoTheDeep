package org.firstinspires.ftc.teamcode.auton.odom.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.RightOZOnlyRunner;


@Autonomous(name="Right/OZOnly [**   ]", group="Blue")
public class RightOZOnly extends LinearOpMode {
	@Override
	public void runOpMode() {
		new RightOZOnlyRunner().run(this);
	}
}