package org.firstinspires.ftc.teamcode.auton.odom.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.LeftSingleSpecimenRunner;


@Autonomous(name="Left/SingleSpecimen [**   ]", group="Blue")
public class LeftSingleSpecimen extends LinearOpMode {
	@Override
	public void runOpMode() {
		new LeftSingleSpecimenRunner().run(this);
	}
}