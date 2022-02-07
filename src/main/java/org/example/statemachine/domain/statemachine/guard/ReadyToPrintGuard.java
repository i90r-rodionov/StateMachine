package org.example.statemachine.domain.statemachine.guard;

import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;


public class ReadyToPrintGuard extends BaseGuard {

    public ReadyToPrintGuard() {
        super();
    }

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = true;


        super.setFlag(flag);
        return super.evaluate(context);
    }
}
