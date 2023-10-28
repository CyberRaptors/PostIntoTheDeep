package org.firstinspires.ftc.teamcode.auton.tempbackup;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

@Autonomous(name="Blue/TempBackup/Park")
public class TempBlueParkBackup extends LinearOpMode {
    RaptorRobot bot = new RaptorRobot();
    ElapsedTime runtime = new ElapsedTime();

    public void strafeLeft(long ms) {
        bot.rightFront.setPower(1);
        bot.rightBack.setPower(-1);
        bot.leftFront.setPower(-1);
        bot.leftBack.setPower(1);

        sleep(ms);

        bot.rightFront.setPower(0);
        bot.rightBack.setPower(0);
        bot.leftFront.setPower(0);
        bot.leftBack.setPower(0);
    }
    public void runOpMode() {
        bot.init(hardwareMap);
        bot.leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        bot.leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        runtime.reset();

        strafeLeft(1750);
    }
}
