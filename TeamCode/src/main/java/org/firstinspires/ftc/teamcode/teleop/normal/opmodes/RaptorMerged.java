package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.normal.runners.RaptorMergedRunner;

@TeleOp(name="TeleOp/Merged", group="Linear Opmode")
public class RaptorMerged extends LinearOpMode {
    @Override
    public void runOpMode() {
        new RaptorMergedRunner().run(this);
    }
}
