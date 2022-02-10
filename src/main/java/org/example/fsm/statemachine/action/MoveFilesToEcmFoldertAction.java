package org.example.fsm.statemachine.action;

import org.example.fsm.service.mock.MockService;
import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.persist.StateHolder;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;


@Service("moveFilesToEcmFolderAction")
public class MoveFilesToEcmFoldertAction extends SaveStateAction {

    private final MockService service;

    public MoveFilesToEcmFoldertAction(StateHolder stateHolder, MockService service) {
        super(stateHolder);
        this.service = service;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {

        trace();

        boolean success = service.moveFiles();

        if (!success) {
            System.out.println("   ### Process Error");
            throw new RuntimeException(this.getClass().getSimpleName() + " Error");
        }
        System.out.println("   ### Process Successful");

        super.execute(context);
    }
}