package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.normal.runners.RaptorAlternateRunner;

@TeleOp(name="TeleOp/AlternateRobot", group="Linear Opmode")
public class RaptorAlternate extends LinearOpMode {
    @Override
    public void runOpMode() {
        new RaptorAlternateRunner().run(this);
    }
}
