package org.firstinspires.ftc.teamcode.raptor.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.raptor.robot.RaptorRobot;

@TeleOp(name="TeleOp/Tests", group="Linear Opmode")
public class RaptorTests extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    RaptorRobot bot = new RaptorRobot();

    private double planeLauncherPower = 0;
    private double hangServoPosition = 0.6;

    public void debugLogOverTelemetry(String message)
    {
        telemetry.addData("dbg", message);
        telemetry.update();
    }

    public void initOpMode() {
        this.bot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();
    }

    public void testPlaneLauncher() {
        if (gamepad2.dpad_up) {
            planeLauncherPower = 0.7;
        }

        if (gamepad2.dpad_down) {
            planeLauncherPower = 0;
        }

        bot.planeLauncher.setPower(planeLauncherPower);
    }

    public void testLift() {
        bot.testLift1.setPower(gamepad2.left_stick_y);
    }

    public void testWheels() {
        bot.rightFront.setPower(-gamepad1.right_stick_y-gamepad1.right_stick_x);
        bot.leftFront.setPower(gamepad1.left_stick_y-gamepad1.left_stick_x);
        bot.rightBack.setPower(-gamepad1.right_stick_y+gamepad1.right_stick_x);
        bot.leftBack.setPower(gamepad1.left_stick_y+gamepad1.left_stick_x);

        // strafe

    }

    public void testHangServo() {
        if (gamepad2.dpad_right) {
            hangServoPosition+=0.001;
        }

        if (gamepad2.dpad_left) {
            hangServoPosition-=0.001;
        }

        bot.hangServo.setPosition(hangServoPosition);
    }

    @Override
    public void runOpMode() {
        initOpMode();

        int counter = 0;

        while (opModeIsActive()) {
            testPlaneLauncher();
            testWheels();
            testHangServo();
            testLift();

            telemetry.addData("Plane Launcher Power", planeLauncherPower);
            telemetry.addData("Lift 1", -bot.testLift1.getPower());
            telemetry.addData("Hang Servo Position", -bot.hangServo.getPosition());
            telemetry.update();

            counter++;
        }
    }
}