package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.normal.runners.REVRaptorRunner;

@TeleOp(name="TeleOp/REVRaptor", group="Linear Opmode")
public class REVRaptor extends LinearOpMode {
    @Override
    public void runOpMode() {
        new REVRaptorRunner().run(this);
    }
}
