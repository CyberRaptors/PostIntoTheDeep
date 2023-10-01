package lib8812.common.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class ITeleopRunner {
    public IDriveableRobot bot;
    public LinearOpMode opMode;
    public ElapsedTime runtime = new ElapsedTime();

    // synonyms
    public Gamepad gamepad1;
    public Gamepad gamepad2;
    public Telemetry telemetry;
    public HardwareMap hardwareMap;
    public void sleep(long ms) {
        opMode.sleep(ms);
    }
    public boolean opModeIsActive() { return opMode.opModeIsActive(); }

    public void initializeOpMode(LinearOpMode opMode) {
        this.opMode = opMode;
        gamepad1 = opMode.gamepad1;
        gamepad2 = opMode.gamepad2;
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
