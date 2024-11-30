package org.firstinspires.ftc.teamcode.auton.odom.opmodes.right;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.right.RightSingleSpecimenRunner;


@Autonomous(name="Right [s+oo/p]", group="Blue")
public class RightSingleSpecimen extends LinearOpMode {
	@Override
	public void runOpMode() {
		new RightSingleSpecimenRunner()    .run(this);
	}
}