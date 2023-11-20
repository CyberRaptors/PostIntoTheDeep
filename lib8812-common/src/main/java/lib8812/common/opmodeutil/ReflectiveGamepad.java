package lib8812.common.opmodeutil;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.function.Function;

public class ReflectiveGamepad extends Gamepad {
    public String[] commonKeyList = {
            "dpad_up", "dpad_down", "dpad_left", "dpad_right",
            "right_bumper", "left_bumper", "right_trigger", "left_trigger",
            "right_stick_button", "left_stick_button",
            "right_stick_y", "left_stick_y", "right_stick_x", "left_stick_x",
            "x", "y", "a", "b"
    };
    public boolean getActivated(String name) { return getPressed(name); }
    public boolean getPressed(String name) {
        switch (name) {
            case "dpad_up":
                return dpad_up;
            case "dpad_down":
                return dpad_down;
            case "dpad_left":
                return dpad_left;
            case "dpad_right":
                return dpad_right;
            case "dpad":
                return dpad_up || dpad_down || dpad_left || dpad_right;
            case "right_bumper":
                return right_bumper;
            case "left_bumper":
                return left_bumper;
            case "right_stick_button":
                return right_stick_button;
            case "left_stick_button":
                return left_stick_button;
            case "x":
                return x;
            case "y":
                return y;
            case "a":
                return a;
            case "b":
                return b;

            case "right_trigger":
                return right_trigger != 0;
            case "left_trigger":
                return left_trigger != 0;
            case "right_stick_x":
                return right_stick_x != 0;
            case "right_stick_y":
                return right_stick_y != 0;
            case "left_stick_x":
                return left_stick_x != 0;
            case "left_stick_y":
                return left_stick_y != 0;

            case "right_stick":
                return getActivated("right_stick_x") || getActivated("right_stick_y");
            case "left_stick":
                return getActivated("left_stick_x") || getActivated("left_stick_y");
        }

        throw new IllegalArgumentException(name);
    }

    public double getValue(String name) {
        switch (name) {
            case "dpad_up":
                return dpad_up ? 1 : 0;
            case "dpad_down":
                return dpad_down ? 1 : 0;
            case "dpad_left":
                return dpad_left ? 1 : 0;
            case "dpad_right":
                return dpad_right ? 1 : 0;
            case "right_bumper":
                return right_bumper ? 1 : 0;
            case "left_bumper":
                return left_bumper ? 1 : 0;
            case "right_stick_button":
                return right_stick_button ? 1 : 0;
            case "left_stick_button":
                return left_stick_button ? 1 : 0;
            case "x":
                return x ? 1 : 0;
            case "y":
                return y ? 1 : 0;
            case "a":
                return a ? 1 : 0;
            case "b":
                return b ? 1 : 0;

            case "right_trigger":
                return right_trigger;
            case "left_trigger":
                return left_trigger;
            case "right_stick_x":
                return right_stick_x;
            case "right_stick_y":
                return right_stick_y;
            case "left_stick_x":
                return left_stick_x;
            case "left_stick_y":
                return left_stick_y;

            case "right_stick":
                Math.max(right_stick_x, right_stick_y);
            case "left_stick":
                Math.max(left_stick_x, left_stick_y);
        }

        throw new IllegalArgumentException(name);
    }
}
