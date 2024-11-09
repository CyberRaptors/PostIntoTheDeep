package org.firstinspires.ftc.teamcode.teleop.odom.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.odom.runners.MergedLocalizationTestRunner;

@TeleOp(name="TeleOp/Merged/LocalizationTest", group="Linear Opmode")
public class MergedLocalizationTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        new MergedLocalizationTestRunner().run(this);
    }
}
