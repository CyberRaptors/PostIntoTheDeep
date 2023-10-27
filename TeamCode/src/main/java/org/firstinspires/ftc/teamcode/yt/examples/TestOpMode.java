package org.firstinspires.ftc.teamcode.yt.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

public class TestOpMode extends LinearOpMode {
    TestRobot bot = new TestRobot();
    ElapsedTime runtime = new ElapsedTime();

    public void driveRobot() {
        bot.leftBack.setPower(gamepad1.left_stick_y);
        bot.leftFront.setPower(gamepad1.left_stick_y);

        bot.rightBack.setPower(gamepad1.right_stick_y);
        bot.rightFront.setPower(gamepad1.right_stick_y);
    }

    @Override
    public void runOpMode() {
        bot.init(hardwareMap);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            driveRobot();

            bot.spinny.setPower(gamepad2.right_trigger);

            telemetry.addData("left wheel power", "back (%.2f) front (%.2f)", bot.leftBack.getPower(), bot.leftFront.getPower());
            telemetry.update();
        }
    }
}
