package org.example.core.statemachine.guard;

import org.example.core.service.mock.CheckService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component("checkCreatedTaskInPega")
public class CreateTaskInPegaGuard extends BaseGuard {

    public CreateTaskInPegaGuard(CheckService checkService) {
        super(checkService);
    }

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = true;

        setFlag(flag);

        return super.evaluate(context);
    }
}
