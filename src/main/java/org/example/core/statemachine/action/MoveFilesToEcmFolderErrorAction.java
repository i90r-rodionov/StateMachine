package org.example.core.statemachine.action;

import org.example.core.service.mock.MockService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.persist.StateHolder;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


@Component("moveFilesToEcmFolderErrorAction")
public class MoveFilesToEcmFolderErrorAction extends BaseAction {

    private final MockService service;

    public MoveFilesToEcmFolderErrorAction(StateHolder stateHolder, MockService service) {
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
