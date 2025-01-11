package org.firstinspires.ftc.teamcode.auton.odom.opmodes.right;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.right.RightSpecimenCycleRunner;


@Autonomous(name="Right [s+ss/p]", group="Blue")
public class RightSpecimenCycle extends LinearOpMode {
	@Override
	public void runOpMode() {
		new RightSpecimenCycleRunner().run(this);
	}
}