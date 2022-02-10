package org.example.fsm.statemachine.guard;

import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;


@Service("checkFilesResend")
public class FilesResendGuard extends BaseGuard {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = true;

        setFlag(flag);

        return super.evaluate(context);
    }
}
