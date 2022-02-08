package org.example.fsm.statemachine.guard;

import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;

import java.util.Set;


public class ReadyToSignGuard extends BaseGuard {

    Set<FsmEvent> signEvents = Set.of(FsmEvent.CHECK_READY_TO_SIGN, FsmEvent.SIGN);

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = signEvents.contains(context.getTransition().getTrigger().getEvent());

        setFlag(flag);

        return super.evaluate(context);
    }
}
