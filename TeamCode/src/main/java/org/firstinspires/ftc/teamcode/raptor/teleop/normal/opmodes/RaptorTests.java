package org.firstinspires.ftc.teamcode.raptor.teleop.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.raptor.teleop.normal.runners.RaptorMainRunner;
import org.firstinspires.ftc.teamcode.raptor.teleop.normal.runners.RaptorTestRunner;

@TeleOp(name="TeleOp/Tests", group="Linear Opmode")
public class RaptorTests extends LinearOpMode {
    @Override
    public void runOpMode() {
        new RaptorTestRunner().run(this);
    }
}
