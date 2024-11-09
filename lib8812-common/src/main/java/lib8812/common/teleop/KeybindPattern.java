package lib8812.common.teleop;

import java.util.HashMap;
import java.util.function.Function;

public class KeybindPattern {
    interface BindingFunction<TBindFunctionType> {
        void apply(String key, TBindFunctionType action);
    }

    public interface ValueBoundFunction {
        void apply(float inp);
    }

    public class GamepadBinder {
        String key;
        
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
        String key;
        BindingFunction<Runnable> registerPress;
        BindingFunction<ValueBoundFunction> registerValue;

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
    
    ReflectiveGamepad gamepad1, gamepad2;

    HashMap<String, Runnable> onePressBinds = new HashMap<>();
    HashMap<String, ValueBoundFunction> oneValueBinds = new HashMap<>();

    HashMap<String, Runnable> twoPressBinds = new HashMap<>();
    HashMap<String, ValueBoundFunction> twoValueBinds = new HashMap<>();

    public KeybindPattern(ReflectiveGamepad gamepad1, ReflectiveGamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }
    
    public GamepadBinder bind(String key) {
        return new GamepadBinder(key)
;    }

    public void registerOnGamepad1(String key, Runnable action) {
        onePressBinds.put(key, action);
    }

    public void registerOnGamepad1(String key, ValueBoundFunction action) {
        oneValueBinds.put(key, action);
    }

    public void registerOnGamepad2(String key, Runnable action) {
        twoPressBinds.put(key, action);
    }

    public void registerOnGamepad2(String key, ValueBoundFunction action) {
        twoValueBinds.put(key, action);
    }

    public void executeActions() {
        for (String key : onePressBinds.keySet()) {
            if (gamepad1.getPressed(key)) {
                onePressBinds.get(key).run();
            }
        }

        for (String key : oneValueBinds.keySet()) {
            oneValueBinds.get(key).apply(gamepad1.getValue(key));
        }


        for (String key : twoPressBinds.keySet()) {
            if (gamepad2.getPressed(key)) {
                twoPressBinds.get(key).run();
            }
        }

        for (String key : twoValueBinds.keySet()) {
            twoValueBinds.get(key).apply(gamepad2.getValue(key));
        }
    }
}
