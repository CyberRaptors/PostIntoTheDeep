package lib8812.common.teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.function.Function;

public class ReflectiveGamepad {
    public class Mapper {
        final String key;

        Mapper(String key) {
            this.key = key;
        }

        public MapExtender to(Function<Float, Integer> action) {
            action.apply(getValue(key));

            return new MapExtender();
        }

        public MapExtender to(Runnable action) {
            if (getPressed(key)) {
                action.run();
            }

            return new MapExtender();
        }
    }

    public class BooleanMapper {
        final boolean condition;

        BooleanMapper(boolean condition) {
            this.condition = condition;
        }

        public MapExtender to(Runnable action) {
            if (condition) {
                action.run();
            }

            return new MapExtender();
        }
    }

    public class MapExtender {
        MapExtender() { }

        public Mapper and(String key) {
            return new Mapper(key);
        }

        public BooleanMapper and(Boolean condition) {
            return new BooleanMapper(condition);
        }
    }

    public final String[] commonKeyList = {
            "dpad_up", "dpad_down", "dpad_left", "dpad_right",
            "right_bumper", "left_bumper", "right_trigger", "left_trigger",
            "right_stick_button", "left_stick_button",
            "right_stick_y", "left_stick_y", "right_stick_x", "left_stick_x",
            "x", "y", "a", "b"
    };

    public final Gamepad inner;

    public ReflectiveGamepad(Gamepad inner) {
        this.inner = inner;
    }

    public boolean getActivated(String name) { return getPressed(name); }
    public boolean getPressed(String name) {
        switch (name) {
            case "dpad_up":
                return inner.dpad_up;
            case "dpad_down":
                return inner.dpad_down;
            case "dpad_left":
                return inner.dpad_left;
            case "dpad_right":
                return inner.dpad_right;
            case "dpad":
                return inner.dpad_up || inner.dpad_down || inner.dpad_left || inner.dpad_right;
            case "right_bumper":
                return inner.right_bumper;
            case "left_bumper":
                return inner.left_bumper;
            case "right_stick_button":
                return inner.right_stick_button;
            case "left_stick_button":
                return inner.left_stick_button;
            case "x":
                return inner.x;
            case "y":
                return inner.y;
            case "a":
                return inner.a;
            case "b":
                return inner.b;

            case "right_trigger":
                return inner.right_trigger != 0;
            case "left_trigger":
                return inner.left_trigger != 0;
            case "right_stick_x":
                return inner.right_stick_x != 0;
            case "right_stick_y":
                return inner.right_stick_y != 0;
            case "left_stick_x":
                return inner.left_stick_x != 0;
            case "left_stick_y":
                return inner.left_stick_y != 0;

            case "right_stick":
                return getActivated("right_stick_x") || getActivated("right_stick_y");
            case "left_stick":
                return getActivated("left_stick_x") || getActivated("left_stick_y");
        }

        throw new IllegalArgumentException(name);
    }

    public float getValue(String name) {
        switch (name) {
            case "dpad_up":
                return inner.dpad_up ? 1 : 0;
            case "dpad_down":
                return inner.dpad_down ? 1 : 0;
            case "dpad_left":
                return inner.dpad_left ? 1 : 0;
            case "dpad_right":
                return inner.dpad_right ? 1 : 0;
            case "right_bumper":
                return inner.right_bumper ? 1 : 0;
            case "left_bumper":
                return inner.left_bumper ? 1 : 0;
            case "right_stick_button":
                return inner.right_stick_button ? 1 : 0;
            case "left_stick_button":
                return inner.left_stick_button ? 1 : 0;
            case "x":
                return inner.x ? 1 : 0;
            case "y":
                return inner.y ? 1 : 0;
            case "a":
                return inner.a ? 1 : 0;
            case "b":
                return inner.b ? 1 : 0;

            case "right_trigger":
                return inner.right_trigger;
            case "left_trigger":
                return inner.left_trigger;
            case "right_stick_x":
                return inner.right_stick_x;
            case "right_stick_y":
                return inner.right_stick_y;
            case "left_stick_x":
                return inner.left_stick_x;
            case "left_stick_y":
                return inner.left_stick_y;

            case "right_stick":
                return Math.max(inner.right_stick_x, inner.right_stick_y);
            case "left_stick":
                return Math.max(inner.left_stick_x, inner.left_stick_y);
        }

        throw new IllegalArgumentException(name);
    }

    public Mapper map(String key) {
        return new Mapper(key);
    }

    public BooleanMapper map(Boolean condition) {
        return new BooleanMapper(condition);
    }
}
