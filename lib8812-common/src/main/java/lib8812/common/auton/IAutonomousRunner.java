package lib8812.common.auton;

import android.content.Context;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcontroller.internal.FtcOpModeRegister;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import lib8812.common.rr.drive.SampleMecanumDrive;
import lib8812.common.teleop.IDriveableRobot;

import java.lang.reflect.InvocationTargetException;

public abstract class IAutonomousRunner<TLabelEnum extends IModelLabel> {
    IDriveableRobot bot;
    ElapsedTime runtime = new ElapsedTime();
    protected SampleMecanumDrive drive;
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
        drive = new SampleMecanumDrive(opMode.hardwareMap);
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

        FtcDashboard.start(null);

        try {
            result = this.<ObjectDetector> getDetectionResult(objectDetectorClass, defaultLabel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        initializeOpMode(opMode);
        internalRun(result);

        FtcDashboard.stop(null);
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
    protected abstract IDriveableRobot getBot();
}
