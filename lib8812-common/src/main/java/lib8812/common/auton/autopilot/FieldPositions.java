package lib8812.common.auton.autopilot;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public class FieldPositions {
    public static final double FIELD_LENGTH_IN = 141;
    public static final double HALF_FIELD_LENGTH_IN = FIELD_LENGTH_IN/2;
    public static final double BLOCK_LENGTH_IN = FIELD_LENGTH_IN/6;
    public static final double HALF_BLOCK_LENGTH_IN = BLOCK_LENGTH_IN/2;

    public static class Landmarks {
        public static final Pose2d FIELD_CENTER = new Pose2d(0, 0, 0);
        public static final Pose2d TOP_LEFT_CORNER = new Pose2d(HALF_FIELD_LENGTH_IN, HALF_FIELD_LENGTH_IN, 0);
        public static final Pose2d TOP_RIGHT_CORNER = new Pose2d(HALF_FIELD_LENGTH_IN, -HALF_FIELD_LENGTH_IN, 0);
        public static final Pose2d BOTTOM_LEFT_CORNER = new Pose2d(-HALF_FIELD_LENGTH_IN, HALF_FIELD_LENGTH_IN, 0);
        public static final Pose2d BOTTOM_RIGHT_CORNER = new Pose2d(-HALF_FIELD_LENGTH_IN, -HALF_FIELD_LENGTH_IN, 0);
    }

    public static class Autonomous { // TODO: check if the degree value is the right orientation
        public static final Pose2d BLUE_LEFT_START = new Pose2d(HALF_BLOCK_LENGTH_IN, HALF_FIELD_LENGTH_IN-HALF_BLOCK_LENGTH_IN, Math.toRadians(90));
        public static final Pose2d RED_LEFT_START = new Pose2d(HALF_BLOCK_LENGTH_IN, -(HALF_FIELD_LENGTH_IN-HALF_BLOCK_LENGTH_IN), Math.toRadians(270));
        public static final Pose2d BLUE_RIGHT_START = new Pose2d(-(HALF_BLOCK_LENGTH_IN+BLOCK_LENGTH_IN), HALF_FIELD_LENGTH_IN-HALF_BLOCK_LENGTH_IN, Math.toRadians(90));
        public static final Pose2d RED_RIGHT_START = new Pose2d(-(HALF_BLOCK_LENGTH_IN+BLOCK_LENGTH_IN), -(HALF_FIELD_LENGTH_IN-HALF_BLOCK_LENGTH_IN), Math.toRadians(270));
    }

    public static class InFrontOf {
        public static final Pose2d BLUE_BACKDROP = new Pose2d(BLOCK_LENGTH_IN*2, HALF_BLOCK_LENGTH_IN+BLOCK_LENGTH_IN, 0);
        public static final Pose2d RED_BACKDROP = new Pose2d(BLOCK_LENGTH_IN*2, -(HALF_BLOCK_LENGTH_IN+BLOCK_LENGTH_IN), 0);
    }
}
