package org.firstinspires.ftc.teamcode.raptor.teleop.odom.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.raptor.teleop.normal.runners.RaptorTestRunner;
import org.firstinspires.ftc.teamcode.raptor.teleop.odom.runners.OdomMainRunner;

@TeleOp(name="Odom/Main", group="Linear Opmode")
public class OdomMain extends LinearOpMode {
    @Override
    public void runOpMode() {
        new OdomMainRunner().run(this);
    }
}
