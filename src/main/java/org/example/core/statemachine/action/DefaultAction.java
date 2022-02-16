package org.example.core.statemachine.action;

import org.example.core.service.mock.MockService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.persist.StateHolder;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component("defaultAction")
public class DefaultAction extends BaseAction {

    private final MockService service;

    public DefaultAction(StateHolder stateHolder, MockService service) {
        super(stateHolder);
        this.service = service;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {

        boolean success = service.defaultAction();

        if (!success) {
            generateError();
        }
        super.execute(context);
    }
}
