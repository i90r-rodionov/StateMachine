package org.example.fsm.statemachine.action;

import org.example.fsm.integration.service.EcmIntegration;
import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.persist.StateHolder;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;


@Service
public class CreateEcmFolderAction extends SaveStateAction {

    private final EcmIntegration service;

    public CreateEcmFolderAction(StateHolder stateHolder, EcmIntegration service) {
        super(stateHolder);
        this.service = service;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {

        boolean success = service.createFolder();

        if (!success) {
            System.out.println("   ### CreateEcmFolderAction Error");
            throw new RuntimeException(this.getClass().getSimpleName() + " Error");
        }

        System.out.println("   ### CreateEcmFolderAction Success");

        super.execute(context);
    }
}
