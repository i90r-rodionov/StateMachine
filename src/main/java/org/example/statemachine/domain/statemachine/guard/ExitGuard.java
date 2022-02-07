package org.example.statemachine.domain.statemachine.guard;

import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;


public class ExitGuard extends BaseGuard {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {

        boolean flag = context.getTransition().getTrigger().getEvent() == FsmEvent.EXIT_EVENT;

        setFlag(flag);

        return super.evaluate(context);
    }
}
