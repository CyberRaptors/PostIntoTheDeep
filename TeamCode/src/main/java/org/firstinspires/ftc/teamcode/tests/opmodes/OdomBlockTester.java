package org.firstinspires.ftc.teamcode.tests.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.tests.runners.OdomBlockTesterRunner;

@TeleOp(name="Odom/BlockTester", group="Tests")
public class OdomBlockTester extends LinearOpMode {
    @Override
    public void runOpMode() {
        new OdomBlockTesterRunner().run(this);
    }
}
