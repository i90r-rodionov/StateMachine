package org.example.fsm.statemachine.guard;

import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;


public class ExitGuard extends BaseGuard {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {

        boolean flag = context.getTransition().getTrigger().getEvent() == FsmEvent.EXIT_EVENT;

        setFlag(flag);

        return super.evaluate(context);
    }
}
