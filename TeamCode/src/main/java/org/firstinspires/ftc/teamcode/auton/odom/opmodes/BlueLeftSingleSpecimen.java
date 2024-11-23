package org.firstinspires.ftc.teamcode.auton.odom.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.BlueLeftSingleSpecimenRunner;


@Autonomous(name="Blue/Left/SingleSpecimen [**   ]", group="Blue")
public class BlueLeftSingleSpecimen extends LinearOpMode {
	@Override
	public void runOpMode() {
		new BlueLeftSingleSpecimenRunner().run(this);
	}
}