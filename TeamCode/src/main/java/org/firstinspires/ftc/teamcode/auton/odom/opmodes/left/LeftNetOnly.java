package org.firstinspires.ftc.teamcode.auton.odom.opmodes.left;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.left.LeftNetOnlyRunner;


@Autonomous(name="Left [0+nn/a]", group="Blue")
public class LeftNetOnly extends LinearOpMode {
	@Override
	public void runOpMode() {
		new LeftNetOnlyRunner().run(this);
	}
}