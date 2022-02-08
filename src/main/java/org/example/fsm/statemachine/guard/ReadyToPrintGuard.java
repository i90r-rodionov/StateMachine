package org.example.fsm.statemachine.guard;

import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.state.FsmState;
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
