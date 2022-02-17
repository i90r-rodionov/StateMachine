package org.example.core.statemachine.guard;

import org.example.core.service.mock.CheckService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component("checkReadyToPrint")
public class ReadyToPrintGuard extends BaseGuard {

    public ReadyToPrintGuard(CheckService checkService) {
        super(checkService);
    }

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = getCheckService().checkReadyToPrint();

        setFlag(flag);

        return super.evaluate(context);
    }
}
