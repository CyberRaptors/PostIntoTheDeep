package lib8812.common.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import lib8812.common.robot.IDriveableRobot;

public abstract class ITeleopRunner {
    public IDriveableRobot bot;
    public LinearOpMode opMode;
    public ElapsedTime runtime = new ElapsedTime();

    // synonyms
    public ReflectiveGamepad gamepad1;
    public ReflectiveGamepad gamepad2;
    public Telemetry telemetry;
    public HardwareMap hardwareMap;
    public void sleep(long ms) {
        opMode.sleep(ms);
    }
    public boolean opModeIsActive() { return opMode.opModeIsActive(); }

    public void debugLogOverTelemetry(String message)
    {
        telemetry.addData("dbg", message);
        telemetry.update();
    }

    protected static CompletableFuture setTimeout(Runnable runnable, int delay) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.MILLISECONDS.sleep(delay); }
            catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            runnable.run();

            return 0;
        });

        return future;
    }

    public void initializeOpMode(LinearOpMode opMode) {
        this.opMode = opMode;

        gamepad1 = new ReflectiveGamepad(opMode.gamepad1); // make sure the OpMode updates our ReflectiveGamepads
        gamepad2 = new ReflectiveGamepad(opMode.gamepad2);

        telemetry = opMode.telemetry;
        hardwareMap = opMode.hardwareMap;

        bot = getBot();
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
