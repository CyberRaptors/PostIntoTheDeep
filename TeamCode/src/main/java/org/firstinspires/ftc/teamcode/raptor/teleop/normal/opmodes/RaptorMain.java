package org.firstinspires.ftc.teamcode.raptor.teleop.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.raptor.teleop.normal.runners.RaptorMainRunner;

@TeleOp(name="TeleOp/Main", group="Linear Opmode")
public class RaptorMain extends LinearOpMode {
    @Override
    public void runOpMode() {
        new RaptorMainRunner().run(this);
    }
}
