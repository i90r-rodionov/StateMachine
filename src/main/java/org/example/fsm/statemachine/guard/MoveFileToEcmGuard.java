package org.example.fsm.statemachine.guard;

import lombok.RequiredArgsConstructor;
import org.example.fsm.integration.service.EcmIntegration;
import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MoveFileToEcmGuard extends BaseGuard {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = true;

        super.setFlag(flag);
        return super.evaluate(context);
    }
}
