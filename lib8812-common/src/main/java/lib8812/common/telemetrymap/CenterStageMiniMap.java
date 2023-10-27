package lib8812.common.telemetrymap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import lib8812.common.rr.drive.SampleMecanumDrive;

public class CenterStageMiniMap extends IMiniMap<CenterStageFieldComponent> {
    public CenterStageMiniMap(LinearOpMode opMode, SampleMecanumDrive drive) { super(opMode, drive); }

    public void update(CenterStageFieldComponent[][] map, int garbage) {
        this.map = map;
    }

    public void printToTelemetry() {
        for (CenterStageFieldComponent[] row : map) {
            StringBuilder lineBuilder = new StringBuilder();
            for (CenterStageFieldComponent fieldElement : row) {
                switch (fieldElement) {
                    case EMPTY_MAT:
                        lineBuilder.append('*');
                        break;
                    case TRIANGULAR_BEAM:
                        lineBuilder.append('^');
                        break;
                    case TRIANGULAR_BEAM_GATE:
                        lineBuilder.append('-');
                        break;
                    case BACKBOARD:
                        lineBuilder.append('#');
                        break;
                    case SELF_ROBOT:
                        lineBuilder.append('R');
                        break;
                    case FRIENDLY_ROBOT:
                        lineBuilder.append('T');
                        break;
                    case ENEMY_ROBOT:
                        lineBuilder.append('E');
                        break;
                    case UNIDENTIFIED_ROBOT:
                        lineBuilder.append('U');
                        break;
                }

                lineBuilder.append(' ');
            }

            opMode.telemetry.addLine(lineBuilder.toString());
        }
    }
}
