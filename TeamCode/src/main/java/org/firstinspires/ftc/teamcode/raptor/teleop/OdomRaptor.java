package org.firstinspires.ftc.teamcode.raptor.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.raptor.robot.RaptorRobot;

@TeleOp(name="Odometry/Main", group="Linear Opmode")
public class OdomRaptor extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    RaptorRobot bot = new RaptorRobot();

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

    @Override
    public void runOpMode() {
        initOpMode();

        while (opModeIsActive()) {

        }
    }
}