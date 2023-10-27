package lib8812.common.telemetrymap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import lib8812.common.rr.drive.SampleMecanumDrive;

public class SelectorCenterStageMiniMap extends IMiniMap<CenterStageFieldComponent> {
    int selectorX = 0, selectorY = 0;

    public SelectorCenterStageMiniMap(LinearOpMode opMode, SampleMecanumDrive drive) { super(opMode, drive); }
    public void update(CenterStageFieldComponent[][] map, int counter) {
        this.map = map;

        if (counter % 35  == 0) {
            if (opMode.gamepad1.dpad_left) {
                selectorX = Math.max(selectorX - 1, 0);
            } else if (opMode.gamepad1.dpad_right) {
                selectorX = Math.min(selectorX + 1, 5);
            } else if (opMode.gamepad1.dpad_up) {
                selectorY = Math.max(selectorY - 1, 0);
            } else if (opMode.gamepad1.dpad_down) {
                selectorY = Math.min(selectorY + 1, 5);
            }
        }
    }

    public void printToTelemetry() {
        int i, j;
        for (i = 0; i < map.length; i++) {
            CenterStageFieldComponent[] row = map[i];

            StringBuilder lineBuilder = new StringBuilder();

            for (j = 0; j < row.length; j++) {
                CenterStageFieldComponent fieldElement = row[j];

                if (j == 0) {
                    if (i == selectorY && j == selectorX)
                        lineBuilder.append(" [");
                    else
                        lineBuilder.append("  ");
                }

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

                if (i == selectorY) {
                    if (j == selectorX-1) {
                        lineBuilder.append(" [");
                        continue;
                    }

                    if (j == selectorX) {
                        lineBuilder.append("] ");
                        continue;
                    }
                }

                lineBuilder.append("  ");
            }

            opMode.telemetry.addLine(lineBuilder.toString());
        }
    }
}
