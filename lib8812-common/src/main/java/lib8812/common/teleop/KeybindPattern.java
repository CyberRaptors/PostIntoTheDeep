package lib8812.common.teleop;

import java.util.HashMap;
import java.util.Map;

public class KeybindPattern {
    interface BindingFunction<TBindFunctionType> {
        void apply(String key, TBindFunctionType action);
    }

    public interface ValueBoundFunction {
        void apply(float inp);
    }

    public class GamepadBinder {
        final String key;
        
        GamepadBinder(String key) {
            this.key = key;
        }

        public FunctionBinder of(ReflectiveGamepad gamepad) {
            if (gamepad1 == gamepad)
                return new FunctionBinder(key, KeybindPattern.this::registerOnGamepad1, KeybindPattern.this::registerOnGamepad1);

            return new FunctionBinder(key, KeybindPattern.this::registerOnGamepad2, KeybindPattern.this::registerOnGamepad2);
        }
    }

    public static class FunctionBinder {
        final String key;
        final BindingFunction<Runnable> registerPress;
        final BindingFunction<ValueBoundFunction> registerValue;

        FunctionBinder(String key, BindingFunction<Runnable> registerPress, BindingFunction<ValueBoundFunction> registerValue) {
            this.registerPress = registerPress;
            this.registerValue = registerValue;
            this.key = key;
        }

        public void to(Runnable action) {
            registerPress.apply(key, action);
        }

        public void to(ValueBoundFunction action) {
            registerValue.apply(key, action);
        }
    }
    
    final ReflectiveGamepad gamepad1;
	final ReflectiveGamepad gamepad2;

    final HashMap<String, Runnable> onePressBinds = new HashMap<>();
    final HashMap<String, ValueBoundFunction> oneValueBinds = new HashMap<>();

    final HashMap<String, Runnable> twoPressBinds = new HashMap<>();
    final HashMap<String, ValueBoundFunction> twoValueBinds = new HashMap<>();

    final HashMap<String, Boolean> wasDownLastOnOne = new HashMap<>();
    final HashMap<String, Boolean> wasDownLastOnTwo = new HashMap<>();

    public KeybindPattern(ReflectiveGamepad gamepad1, ReflectiveGamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }
    
    public GamepadBinder bind(String key) {
        return new GamepadBinder(key)
;    }

    public void registerOnGamepad1(String key, Runnable action) { onePressBinds.put(key, action); }

    public void registerOnGamepad1(String key, ValueBoundFunction action) {
        oneValueBinds.put(key, action);
    }

    public void registerOnGamepad2(String key, Runnable action) { twoPressBinds.put(key, action); }

    public void registerOnGamepad2(String key, ValueBoundFunction action) {
        twoValueBinds.put(key, action);
    }

    public void executeActions() {
        for (Map.Entry<String, Runnable> entry : onePressBinds.entrySet()) {
            if (gamepad1.getPressed(entry.getKey())) {
                wasDownLastOnOne.put(entry.getKey(), true);
            } else if (Boolean.TRUE.equals(wasDownLastOnOne.get(entry.getKey()))) {
                wasDownLastOnOne.put(entry.getKey(), false);
                entry.getValue().run(); // run the callback, a push down and release constitutes a full button press/click
            }
        }

        for (Map.Entry<String, ValueBoundFunction> entry : oneValueBinds.entrySet()) {
            entry.getValue().apply(gamepad1.getValue(entry.getKey()));
        }

        for (Map.Entry<String, Runnable> entry : twoPressBinds.entrySet()) {
            if (gamepad2.getPressed(entry.getKey())) {
                wasDownLastOnTwo.put(entry.getKey(), true);
            } else if (Boolean.TRUE.equals(wasDownLastOnTwo.get(entry.getKey()))) {
                wasDownLastOnTwo.put(entry.getKey(), false);
                entry.getValue().run(); // run the callback, a push down and release constitutes a full button press/click
            }
        }

        for (Map.Entry<String, ValueBoundFunction> entry : twoValueBinds.entrySet()) {
            entry.getValue().apply(gamepad2.getValue(entry.getKey()));
        }
    }
}
