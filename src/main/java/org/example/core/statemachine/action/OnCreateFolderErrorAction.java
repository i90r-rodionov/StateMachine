package org.example.core.statemachine.action;

import org.example.core.service.mock.CheckService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.persist.StateHolder;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


@Component
public class OnCreateFolderErrorAction extends BaseAction {

    private final CheckService service;

    public OnCreateFolderErrorAction(StateHolder stateHolder, CheckService service) {
        super(stateHolder);
        this.service = service;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {

        boolean success = service.defaultErrorAction();

        if (!success) {
            generateError();
        }
        super.execute(context);
    }
}
