package lib8812.common.auton.autopilot;

import lib8812.common.rr.drive.SampleMecanumDrive;
import lib8812.common.rr.trajectorysequence.TrajectorySequence;
import static lib8812.common.auton.autopilot.FieldPositions.*;

public class TrajectoryLists {
    public static class FromBlueLeft {
        public static TrajectorySequence[] toBlueBackdrop;
    }

    public static class FromBlueRight {
        public static TrajectorySequence[] toBlueBackdrop;
    }

    public static class FromRedRight {
        public static TrajectorySequence[] toRedBackdrop;
    }

    public static class FromRedLeft {
        public static TrajectorySequence[] toRedBackdrop;
    }

    public static void initializeTrajectoryLists(SampleMecanumDrive drive)
    {
        // TODO: add more trajectories for redleft and blueright

        FromRedRight.toRedBackdrop = new TrajectorySequence[] {
                drive.trajectorySequenceBuilder(Autonomous.RED_RIGHT_START)
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(90))
                        .forward(HALF_BLOCK_LENGTH_IN+BLOCK_LENGTH_IN)
                        .build()
        };

        FromRedLeft.toRedBackdrop = new TrajectorySequence[] {
                drive.trajectorySequenceBuilder(Autonomous.RED_LEFT_START)
                        .turn(Math.toRadians(90))
                        .forward(BLOCK_LENGTH_IN)
                        .strafeLeft(BLOCK_LENGTH_IN)
                        .forward((BLOCK_LENGTH_IN*2) + HALF_BLOCK_LENGTH_IN)
                        .build()
        };

        FromBlueLeft.toBlueBackdrop = new TrajectorySequence[] {
                drive.trajectorySequenceBuilder(Autonomous.BLUE_LEFT_START)
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(-90))
                        .forward(HALF_BLOCK_LENGTH_IN + BLOCK_LENGTH_IN)
                        .build()
        };

        FromBlueRight.toBlueBackdrop = new TrajectorySequence[] {
                drive.trajectorySequenceBuilder(Autonomous.BLUE_RIGHT_START)
                        .turn(Math.toRadians(-90))
                        .forward(BLOCK_LENGTH_IN)
                        .strafeRight(BLOCK_LENGTH_IN)
                        .forward((BLOCK_LENGTH_IN*2) + HALF_BLOCK_LENGTH_IN)
                        .build()
        };
    }
}
