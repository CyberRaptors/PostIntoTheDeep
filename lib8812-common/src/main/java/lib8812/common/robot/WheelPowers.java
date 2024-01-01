package lib8812.common.robot;

public class WheelPowers {
    public final double leftFront, leftBack, rightFront, rightBack;

    public WheelPowers(double leftFront, double leftBack, double rightFront, double rightBack) {
        this.leftFront = leftFront;
        this.leftBack = leftBack;
        this.rightFront = rightFront;
        this.rightBack = rightBack;
    }

    public WheelPowers(double[] wheelPowers) {
        this.leftFront = wheelPowers[0];
        this.leftBack = wheelPowers[1];
        this.rightFront = wheelPowers[2];
        this.rightBack = wheelPowers[3];
    }
}
