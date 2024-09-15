package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.robot.IDriveableRobot;
import lib8812.common.robot.WheelPowers;
import lib8812.common.teleop.ITeleOpRunner;
import lib8812.common.teleop.TeleOpUtils;

public class RaptorTestRunner extends ITeleOpRunner {
    RaptorRobot bot = new RaptorRobot();
    private final WheelPowers wheelWeights = new WheelPowers(1, 1, 1, 1);
    boolean showExtraInfo = false;

    protected IDriveableRobot getBot() { return bot; }

    void testWheels() {
        double correctedRightY = TeleOpUtils.fineTuneInput(gamepad1.inner.right_stick_y);
        double correctedRightX = TeleOpUtils.fineTuneInput(gamepad1.inner.right_stick_x);
        double correctedLeftY = TeleOpUtils.fineTuneInput(gamepad1.inner.left_stick_y);
        double correctedLeftX = TeleOpUtils.fineTuneInput(gamepad1.inner.left_stick_x);

        WheelPowers correctedWheelPowers = new WheelPowers(
                (correctedLeftY-correctedLeftX),
                (correctedLeftY+correctedLeftX),
                (-correctedRightY-correctedRightX),
                (-correctedRightY+correctedRightX)
        );

        correctedWheelPowers.applyTo(bot, wheelWeights);
    }

    WheelPowers getRealWheelInputPowers() {
        return new WheelPowers(
                -gamepad1.inner.right_stick_y-gamepad1.inner.right_stick_x,
                gamepad1.inner.left_stick_y-gamepad1.inner.left_stick_x,
                -gamepad1.inner.right_stick_y+gamepad1.inner.right_stick_x,
                gamepad1.inner.left_stick_y+gamepad1.inner.left_stick_x
        );
    }

    void moveExtension() {
        bot.extension.setPosition((gamepad2.inner.right_stick_y+1)*0.5);

        // stick up full -> 0 [out]
        // stick halfway up -> 0.25 [halfway out]
        // stick resting pos -> 0.5 [in]

        // note that gamepad2.inner.right_stick_y seems to give us negative inputs
    }

    void moveLift() {
        bot.mainLift.setPosition(
                bot.mainLift.getPosition()- (int) (gamepad2.inner.left_stick_y*50)
        );
    }

    protected void internalRun() {
        int counter = 0;

        while (opModeIsActive()) {
            testWheels();

            WheelPowers realWheelInputPowers = getRealWheelInputPowers();

            moveExtension();
            moveLift();

            if (counter % 100 == 0)
                gamepad1
                        .map("x")
                        .to(() -> showExtraInfo = !showExtraInfo); // telemetry verbosity

            telemetry.addData(
                    "extension", "pos (%.2f)",
                    bot.extension.getPosition()
            );

            if (showExtraInfo) {
                telemetry.addData(
                        "Wheels (input)",
                        "leftFront (%.2f) leftBack (%.2f) rightFront (%.2f) rightBack (%.2f)",
                        realWheelInputPowers.leftFront,
                        realWheelInputPowers.leftBack,
                        realWheelInputPowers.rightFront,
                        realWheelInputPowers.rightBack
                );
                telemetry.addData(
                        "Wheels (corrected)",
                        "leftFront (%.2f) [tuned by %.2f] leftBack (%.2f) [tuned by %.2f] rightFront (%.2f) [tuned by %.2f] rightBack (%.2f) [tuned by %.2f]",
                        bot.leftFront.getPower(), realWheelInputPowers.leftFront - bot.leftFront.getPower(),
                        bot.leftBack.getPower(), realWheelInputPowers.leftBack - bot.leftBack.getPower(),
                        bot.rightFront.getPower(), realWheelInputPowers.rightFront - bot.rightFront.getPower(),
                        bot.rightBack.getPower(), realWheelInputPowers.rightBack - bot.rightBack.getPower()
                );
            }

            telemetry.addData("Verbosity Level", "%s", showExtraInfo ? "high" : "low");

            if (showExtraInfo) {
                for (String key : gamepad1.commonKeyList) {
                    telemetry.addData("Gamepad 1", "%s (%.2f)", key, gamepad1.getValue(key));
                }

                for (String key : gamepad2.commonKeyList) {
                    telemetry.addData("Gamepad 2", "%s (%.2f)", key, gamepad2.getValue(key));
                }
            }

            telemetry.update();

            counter++;
        }
    }
}