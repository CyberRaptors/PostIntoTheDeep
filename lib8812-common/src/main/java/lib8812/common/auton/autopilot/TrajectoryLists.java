package lib8812.common.auton.autopilot;

import lib8812.common.rr.drive.SampleMecanumDrive;
import lib8812.common.rr.trajectorysequence.TrajectorySequence;
import static lib8812.common.auton.autopilot.FieldPositions.*;

public class TrajectoryLists {
    public static class FromBlueLeft {
        public static TrajectorySequence[] toBlueBackdrop;
    }

    public static class FromRedRight {
        public static TrajectorySequence[] toRedBackdrop;
    }

    public static class FromRedLeft {
        public static class InHarmonious {
            public static TrajectorySequence[] toRedBackdrop;
        }

        public static class Harmonious {
            public static TrajectorySequence[] toRedBackdrop;

        }
    }

    public static class FromBlueRight {
        public static class InHarmonious {
            public static TrajectorySequence[] toBlueBackdrop;
        }

        public static class Harmonious {
            public static TrajectorySequence[] toBlueBackdrop;

        }
    }


    public static void initializeTrajectoryLists(SampleMecanumDrive drive)
    {
        // TODO: add more trajectories for redleft and blueright

        FromRedRight.toRedBackdrop = new TrajectorySequence[] { // includes three turns for now due to strafing inaccuracies, change back once fixed
                drive.trajectorySequenceBuilder(Autonomous.RED_RIGHT_START)
                        .turn(Math.toRadians(-90))
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(90))
                        .forward(HALF_BLOCK_LENGTH_IN+BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(-90))
                        .build()
        };

        FromRedLeft.InHarmonious.toRedBackdrop = new TrajectorySequence[] {
                drive.trajectorySequenceBuilder(Autonomous.RED_LEFT_START)
                        .strafeLeft(BLOCK_LENGTH_IN)
                        .forward(BLOCK_LENGTH_IN*2)
                        .turn(Math.toRadians(90))
                        .forward(BLOCK_LENGTH_IN*4)
                        .strafeRight(BLOCK_LENGTH_IN)
                        .forward(HALF_BLOCK_LENGTH_IN)
                        .build()
        };


        FromRedLeft.Harmonious.toRedBackdrop = new TrajectorySequence[] {
                drive.trajectorySequenceBuilder(Autonomous.RED_LEFT_START)
                        .strafeRight(BLOCK_LENGTH_IN*3)
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(90))
                        .forward(HALF_BLOCK_LENGTH_IN)
                        .build()
        };

        FromBlueLeft.toBlueBackdrop = new TrajectorySequence[] {
                drive.trajectorySequenceBuilder(Autonomous.BLUE_LEFT_START)
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(-90))
                        .forward(HALF_BLOCK_LENGTH_IN + BLOCK_LENGTH_IN)
                        .build()
        };

        FromBlueRight.InHarmonious.toBlueBackdrop = new TrajectorySequence[] {
                drive.trajectorySequenceBuilder(Autonomous.RED_LEFT_START)
                        .strafeRight(BLOCK_LENGTH_IN)
                        .forward(BLOCK_LENGTH_IN*2)
                        .turn(Math.toRadians(-90))
                        .forward(BLOCK_LENGTH_IN*4)
                        .strafeLeft(BLOCK_LENGTH_IN)
                        .forward(HALF_BLOCK_LENGTH_IN)
                        .build()
        };


        FromBlueRight.Harmonious.toBlueBackdrop = new TrajectorySequence[] {
                drive.trajectorySequenceBuilder(Autonomous.RED_LEFT_START)
                        .strafeLeft(BLOCK_LENGTH_IN*3)
                        .forward(BLOCK_LENGTH_IN)
                        .turn(Math.toRadians(-90))
                        .forward(HALF_BLOCK_LENGTH_IN)
                        .build()
        };
    }
}
