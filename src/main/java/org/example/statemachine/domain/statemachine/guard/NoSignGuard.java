package org.example.statemachine.domain.statemachine.guard;

import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;

import java.util.Set;


public class NoSignGuard extends BaseGuard {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = Flags.isNoSignFlag();

        setFlag(flag);

        return super.evaluate(context);
    }
}
