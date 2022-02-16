package org.example.core.statemachine.guard;

import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


@Component("checkDeliveredFiles")
public class DeliveryFilesGuard extends BaseGuard {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = true;

        setFlag(flag);

        return super.evaluate(context);
    }
}
