package lib8812.common.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import lib8812.common.robot.IDriveableRobot;

public abstract class ITeleOpRunner {
    public LinearOpMode opMode;
    public ElapsedTime runtime = new ElapsedTime();

    // synonyms
    protected ReflectiveGamepad gamepad1;
    protected ReflectiveGamepad gamepad2;
    protected KeybindPattern keybinder;
    protected Telemetry telemetry;
    protected HardwareMap hardwareMap;

    protected void sleep(long ms) {
        opMode.sleep(ms);
    }

    protected boolean opModeIsActive() { return opMode.opModeIsActive(); }

    protected void debugLogOverTelemetry(String message)
    {
        telemetry.addData("dbg", message);
        telemetry.update();
    }

    void initializeOpMode(LinearOpMode opMode) {
        this.opMode = opMode;

        gamepad1 = new ReflectiveGamepad(opMode.gamepad1); // make sure the OpMode updates our ReflectiveGamepads
        gamepad2 = new ReflectiveGamepad(opMode.gamepad2);

        keybinder = new KeybindPattern(gamepad1, gamepad2);

        telemetry = opMode.telemetry;
        hardwareMap = opMode.hardwareMap;

        IDriveableRobot bot = getBot();
        bot.init(opMode.hardwareMap);
        opMode.waitForStart();
        runtime.reset();
    }

    public void run(LinearOpMode opMode) {
        initializeOpMode(opMode);
        internalRun();
    }

    protected abstract void internalRun();
    protected abstract IDriveableRobot getBot();
}
