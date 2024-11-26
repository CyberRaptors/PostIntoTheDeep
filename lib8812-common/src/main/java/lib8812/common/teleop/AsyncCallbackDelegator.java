package lib8812.common.teleop;

import android.util.Pair;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.HashMap;

import lib8812.common.util.ZeroArgPredicate;

public class AsyncCallbackDelegator {
    public class CallbackBinderPattern {
        ZeroArgPredicate passedPredicate;
        CallbackBinderPattern(ZeroArgPredicate predicate) {
            passedPredicate = predicate;
        }

        public void thenCall(Runnable callback) {
            callbacks.add(new Pair<>(passedPredicate, callback));
        }
    }
    ArrayList<Pair<ZeroArgPredicate, Runnable>> callbacks = new ArrayList<>();

    public CallbackBinderPattern waitFor(ZeroArgPredicate predicate) {
        return new CallbackBinderPattern(predicate);
    }

    public CallbackBinderPattern waitFor(DcMotor motor) {
        return waitFor(() -> !motor.isBusy());
    }

    public void delegate() {
        for (
                Pair<ZeroArgPredicate, Runnable> callbackPair :
                ((ArrayList<Pair<ZeroArgPredicate, Runnable>>) callbacks.clone())
        ) {
            if (callbackPair.first.run()) { // if the predicate is true...
                callbackPair.second.run(); // run the callback & remove from list
                callbacks.remove(callbackPair);
            }
        }
    }
}
