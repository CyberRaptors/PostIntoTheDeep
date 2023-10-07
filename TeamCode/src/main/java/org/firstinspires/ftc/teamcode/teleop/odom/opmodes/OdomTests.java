package org.firstinspires.ftc.teamcode.teleop.odom.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.odom.runners.OdomTestRunner;

@TeleOp(name="Odom/Tests", group="Linear Opmode")
public class OdomTests extends LinearOpMode {
    @Override
    public void runOpMode() {
        new OdomTestRunner().run(this);
    }
}
