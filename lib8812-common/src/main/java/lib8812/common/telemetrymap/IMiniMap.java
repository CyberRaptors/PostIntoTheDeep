package lib8812.common.telemetrymap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import lib8812.common.rr.drive.SampleMecanumDrive;

public abstract class IMiniMap<TFieldComponentEnum> {
    protected TFieldComponentEnum[][] map;
    protected LinearOpMode opMode;
    protected SampleMecanumDrive drive;

    public IMiniMap(LinearOpMode opMode, SampleMecanumDrive drive) {
        this.opMode = opMode;
        this.drive = drive;
    }
    abstract public void update(TFieldComponentEnum[][] newMap, int counter);
    abstract public void printToTelemetry();
}
