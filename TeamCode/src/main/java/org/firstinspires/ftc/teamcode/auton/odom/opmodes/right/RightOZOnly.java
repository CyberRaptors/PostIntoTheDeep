package org.firstinspires.ftc.teamcode.auton.odom.opmodes.right;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.right.RightOZOnlyRunner;


@Autonomous(name="Right [0+oo/p]", group="Blue")
public class RightOZOnly extends LinearOpMode {
	@Override
	public void runOpMode() {
		new RightOZOnlyRunner().run(this);
	}
}