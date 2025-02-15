package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.normal.runners.RaptorEmergencyRunner;

@TeleOp(name="TeleOp/EmergencyRecovery", group="Linear Opmode")
public class RaptorEmergency extends LinearOpMode {
    @Override
    public void runOpMode() {
        new RaptorEmergencyRunner().run(this);
    }
}
