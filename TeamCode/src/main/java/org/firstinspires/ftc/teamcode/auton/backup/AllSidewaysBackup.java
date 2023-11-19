package org.firstinspires.ftc.teamcode.auton.backup;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

@Autonomous(name="Autonomous/AllSidewaysBackup")
public class AllSidewaysBackup extends LinearOpMode {
    ElapsedTime runtime = new ElapsedTime();
    RaptorRobot bot = new RaptorRobot();

    public void moveForward(long ms) {
        bot.rightFront.setPower(1);
        bot.rightBack.setPower(1);
        bot.leftFront.setPower(-1);
        bot.leftBack.setPower(-1);

        sleep(ms);

        bot.rightFront.setPower(0);
        bot.rightBack.setPower(0);
        bot.leftFront.setPower(0);
        bot.leftBack.setPower(0);
    }
    public void runOpMode() {
        bot.init(hardwareMap);

        waitForStart();
        runtime.reset();

        moveForward(1000);
    }
}
