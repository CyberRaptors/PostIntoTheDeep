package lib8812.common.auton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.Telemetry;

import lib8812.common.robot.IMecanumRobot;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class IAutonomousRunner<TLabelEnum extends IModelLabel> {
    IMecanumRobot bot;
    final ElapsedTime runtime = new ElapsedTime();
    protected LinearOpMode opMode;
    protected IObjectDetector<TLabelEnum> objectDetector;

    // synonyms
    public Telemetry telemetry;
    public HardwareMap hardwareMap;
    public void sleep(long ms) {
        opMode.sleep(ms);
    }

    public boolean opModeIsActive() { return opMode.opModeIsActive(); }

    void initializeOpModeSynonymsAndBot(LinearOpMode opMode) {
        this.opMode = opMode;

        telemetry = opMode.telemetry;
        hardwareMap = opMode.hardwareMap;

        bot = getBot();

        bot.init(opMode.hardwareMap);
    }

    void initializeOpMode(LinearOpMode opMode) {
        this.opMode = opMode;

        opMode.waitForStart();
        runtime.reset();

//        objectDetector.destroy();
    }

    public <ObjectDetector extends IObjectDetector<TLabelEnum>> void run(LinearOpMode opMode, Class<ObjectDetector> objectDetectorClass, TLabelEnum defaultLabel) {
        TLabelEnum result;

        initializeOpModeSynonymsAndBot(opMode);

        try {
            result = this.getDetectionResult(objectDetectorClass, defaultLabel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        initializeOpMode(opMode);

        if (opModeIsActive()) {
            internalRun(result);
//            objectDetector.destroy();
        }

        while (opModeIsActive()); // see if this fixes the restarting issue
    }

    protected static CompletableFuture<Integer> setTimeout(Runnable runnable, int delay) {
		return CompletableFuture.supplyAsync(() -> {
            runnable.run();

            try { TimeUnit.MILLISECONDS.sleep(delay); }
            catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            return 0;
        });
    }

    protected void debugLogOverTelemetry(String message)
    {
        telemetry.addData("dbg", message);
        telemetry.update();
    }

    protected <ObjectDetector extends IObjectDetector<TLabelEnum>> TLabelEnum getDetectionResult(Class<ObjectDetector> objectDetectorClass, TLabelEnum defaultLabel) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        objectDetector = objectDetectorClass.getConstructor(LinearOpMode.class).newInstance(opMode);

        objectDetector.init();

        TLabelEnum res = defaultLabel;

        while (res == defaultLabel)
            res = objectDetector.getCurrentFeed();

        return res;
    }

    protected abstract void internalRun(TLabelEnum result);
    protected abstract IMecanumRobot getBot();
}
