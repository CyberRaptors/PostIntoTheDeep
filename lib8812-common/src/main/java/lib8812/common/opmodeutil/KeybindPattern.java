package lib8812.common.opmodeutil;

import java.util.HashMap;
import java.util.function.Function;

public class KeybindPattern {
    ReflectiveGamepad gamepad1, gamepad2;

    HashMap<String, Runnable> onePressedBinds = new HashMap<>();
    HashMap<String, Function<Double, Object>> oneValueBinds = new HashMap<>();

    HashMap<String, Runnable> twoPressedBinds = new HashMap<>();
    HashMap<String, Function<Double, Object>> twoValueBinds = new HashMap<>();

    public KeybindPattern(ReflectiveGamepad gamepad1, ReflectiveGamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void registerOnGamepad1(String key, Runnable action) {
        onePressedBinds.put(key, action);
    }

    public void registerOnGamepad1(String key, Function<Double, Object> action) {
        oneValueBinds.put(key, action);
    }

    public void registerOnGamepad2(String key, Runnable action) {
        twoPressedBinds.put(key, action);
    }

    public void registerOnGamepad2(String key, Function<Double, Object> action) {
        twoValueBinds.put(key, action);
    }

    public void executeActions() {
        for (String key : onePressedBinds.keySet()) {
            if (gamepad1.getPressed(key)) {
                onePressedBinds.get(key).run();
            }
        }

        for (String key : oneValueBinds.keySet()) {
            oneValueBinds.get(key).apply(gamepad1.getValue(key));
        }


        for (String key : twoPressedBinds.keySet()) {
            if (gamepad2.getPressed(key)) {
                twoPressedBinds.get(key).run();
            }
        }

        for (String key : twoValueBinds.keySet()) {
            twoValueBinds.get(key).apply(gamepad2.getValue(key));
        }
    }
}
