package org.example.fsm.statemachine.guard;

import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;


public class EcmFolderGuard extends BaseGuard {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = false;

        super.setFlag(flag);
        return super.evaluate(context);
    }
}
