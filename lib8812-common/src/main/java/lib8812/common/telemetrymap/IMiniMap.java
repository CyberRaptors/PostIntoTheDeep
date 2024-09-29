package lib8812.common.telemetrymap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class IMiniMap<TFieldComponentEnum> {
    protected TFieldComponentEnum[][] map;
    protected LinearOpMode opMode;

    public IMiniMap(LinearOpMode opMode) {
        this.opMode = opMode;
    }
    abstract public void update(TFieldComponentEnum[][] newMap, int counter);
    abstract public void printToTelemetry();
}
