package org.firstinspires.ftc.teamcode.teleop.odom.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.odom.runners.OdomMainRunner;

@TeleOp(name="Odom/Main", group="Linear Opmode")
public class OdomMain extends LinearOpMode {
    @Override
    public void runOpMode() {
        new OdomMainRunner().run(this);
    }
}
