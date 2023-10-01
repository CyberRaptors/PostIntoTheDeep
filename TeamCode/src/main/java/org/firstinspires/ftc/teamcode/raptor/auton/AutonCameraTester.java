package org.firstinspires.ftc.teamcode.raptor.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.raptor.auton.detectors.PixelDetectionConstants;
import org.firstinspires.ftc.teamcode.raptor.auton.detectors.PrimaryPositionDetector;
import org.firstinspires.ftc.teamcode.raptor.robot.RaptorRobot;
import lib8812.common.auton.IObjectDetector;

@Autonomous(name="Autonomous/CameraTester")
public class AutonCameraTester extends LinearOpMode {
    RaptorRobot bot = new RaptorRobot();
    ElapsedTime runtime = new ElapsedTime();
    public void runOpMode() {
        bot.init(hardwareMap);

        IObjectDetector<PixelDetectionConstants.PixelPosition> objectDetector = new PrimaryPositionDetector(this);

        objectDetector.init();

        waitForStart();
        runtime.reset();

        while (opModeIsActive())
            objectDetector.logInputToTelemetry();

        objectDetector.destroy();
    }
}
