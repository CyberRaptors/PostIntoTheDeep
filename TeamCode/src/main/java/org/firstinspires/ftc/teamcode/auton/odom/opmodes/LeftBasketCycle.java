package org.firstinspires.ftc.teamcode.auton.odom.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.LeftBasketCycleRunner;


@Autonomous(name="Left [b+bbb/a]", group="Blue")
public class LeftBasketCycle extends LinearOpMode {
	@Override
	public void runOpMode() {
		new LeftBasketCycleRunner().run(this);
	}
}