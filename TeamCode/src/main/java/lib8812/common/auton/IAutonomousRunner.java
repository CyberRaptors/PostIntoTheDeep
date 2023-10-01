package lib8812.common.auton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import lib8812.common.teleop.IDriveableRobot;

import java.lang.reflect.InvocationTargetException;

public abstract class IAutonomousRunner<TLabelEnum extends IModelLabel> {
    IDriveableRobot bot;
    ElapsedTime runtime = new ElapsedTime();
    LinearOpMode opMode;

    // synonyms
    public Telemetry telemetry;
    public HardwareMap hardwareMap;
    public void sleep(long ms) {
        opMode.sleep(ms);
    }

    public void initializeOpMode(LinearOpMode opMode) {
        this.opMode = opMode;

        telemetry = opMode.telemetry;
        hardwareMap = opMode.hardwareMap;

        bot.init(opMode.hardwareMap);
        opMode.waitForStart();
        runtime.reset();
    }

    public <ObjectDetector extends IObjectDetector<TLabelEnum>> void run(LinearOpMode opMode, Class<ObjectDetector> objectDetectorClass, TLabelEnum defaultLabel) {
        initializeOpMode(opMode);

        TLabelEnum result;

        try {
            result = this.<ObjectDetector> getDetectionResult(objectDetectorClass, defaultLabel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        internalRun(result);
    }

    protected <ObjectDetector extends IObjectDetector<TLabelEnum>> TLabelEnum getDetectionResult(Class<ObjectDetector> objectDetectorClass, TLabelEnum defaultLabel) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        IObjectDetector<TLabelEnum> objectDetector = objectDetectorClass.getConstructor(LinearOpMode.class).newInstance(opMode);

        objectDetector.init();

        while (objectDetector.getCurrentFeed() == defaultLabel);

        TLabelEnum res = objectDetector.getCurrentFeed();

        objectDetector.destroy();

        return res;
    }

    protected abstract void internalRun(TLabelEnum result);
}
