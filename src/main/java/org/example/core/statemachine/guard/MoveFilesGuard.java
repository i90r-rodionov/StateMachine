package org.example.core.statemachine.guard;

import org.example.core.statemachine.state.FsmState;
import org.example.core.statemachine.event.FsmEvent;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component("checkMovedFiles")
public class MoveFilesGuard extends BaseGuard {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = true;

        setFlag(flag);

        return super.evaluate(context);
    }
}