package lib8812.common.rr;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.Twist2dDual;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Vector2dDual;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.HardwareMap;

import lib8812.common.robot.hardwarewrappers.DegreeInchesOTOS;
import lib8812.common.robot.poses.SimpleDegreePose;

@Config
public final class OTOSLocalizer implements Localizer{
    final DegreeInchesOTOS otos;
    final DegreeInchesOTOS.Configuration otosConfig =
            new DegreeInchesOTOS.Configuration()
                    .withOffset(
                            0, 0, 0
                    )
                    .withStartingPoint(
                            0, 0, 0
                    )
                    .withLinearMultiplier(1)
                    .withAngularMultiplier(1);

    private double lastXPos, lastYPos;
    private Rotation2d lastHeading;


    private double lastRawHeadingVel, headingVelOffset;
    private boolean initialized;

    public OTOSLocalizer(HardwareMap hardwareMap) {
        otos = new DegreeInchesOTOS(
                hardwareMap.get(SparkFunOTOS.class, "otos"),
                otosConfig
        );
    }

    public Twist2dDual<Time> update() {
        SimpleDegreePose pos = otos.getPosition();
        SimpleDegreePose velo = otos.getVelocity();

        Rotation2d heading = Rotation2d.exp(pos.h);

        double rawHeadingVel = velo.h;

        if (Math.abs(rawHeadingVel - lastRawHeadingVel) > Math.PI) {
            headingVelOffset -= Math.signum(rawHeadingVel) * 2 * Math.PI;
        }

        lastRawHeadingVel = rawHeadingVel;
        double headingVel = headingVelOffset + rawHeadingVel;

        if (!initialized) {
            initialized = true;

            lastXPos = pos.x;
            lastYPos = pos.y;
            lastHeading = heading;

            return new Twist2dDual<>(
                    Vector2dDual.constant(new Vector2d(0.0, 0.0), 2),
                    DualNum.constant(0.0, 2)
            );
        }

        double parPosDelta = pos.x - lastXPos;
        double perpPosDelta = pos.y - lastYPos;
        double headingDelta = heading.minus(lastHeading);

        Twist2dDual<Time> twist = new Twist2dDual<>(
                new Vector2dDual<>(
						new DualNum<>(new double[]{
								parPosDelta,
								velo.x,
						}),
                        new DualNum<>(new double[] {
                                perpPosDelta,
                                velo.y,
                        })
                ),
                new DualNum<>(new double[] {
                        headingDelta,
                        headingVel,
                })
        );

        lastXPos = pos.x;
        lastYPos = pos.y;
        lastHeading = heading;

        return twist;
    }
}
