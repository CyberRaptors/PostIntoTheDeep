package lib8812.common.robot;

import java.util.function.Function;

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

    public WheelPowers morph(WheelPowers weightage) {
        return new WheelPowers(
                leftFront*weightage.leftFront,
                leftBack*weightage.leftBack,
                rightFront*weightage.rightFront,
                rightBack*weightage.rightBack
        );
    }

    public WheelPowers morph(Function<Double, Double> simpleFineTuner) {
        return new WheelPowers(
                simpleFineTuner.apply(leftFront),
                simpleFineTuner.apply(leftBack),
                simpleFineTuner.apply(rightFront),
                simpleFineTuner.apply(rightBack)
        );
    }

    public void applyTo(IDriveableRobot bot) {
        bot.leftFront.setPower(leftFront);
        bot.leftBack.setPower(leftBack);
        bot.rightFront.setPower(rightFront);
        bot.rightBack.setPower(rightBack);
    }

    public void applyTo(IDriveableRobot bot, WheelPowers weightage) {
        morph(weightage).applyTo(bot);
    }

    public void applyTo(IDriveableRobot bot, Function<Double, Double> simpleFineTuner) {
        morph(simpleFineTuner).applyTo(bot);
    }

    public void applyTo(IDriveableRobot bot, WheelPowers weightage, Function<Double, Double> simpleFineTuner) {
        morph(simpleFineTuner).morph(weightage).applyTo(bot);
    }
}
