package org.example.fsm.statemachine.action;

import org.example.fsm.integration.service.EcmIntegration;
import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.persist.StateHolder;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;


@Service
public class MoveFilesToEcmAction extends SaveStateAction {

    private final EcmIntegration service;

    public MoveFilesToEcmAction(StateHolder stateHolder, EcmIntegration service) {
        super(stateHolder);
        this.service = service;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {

        boolean success = service.moveFiles();

        String methodName = String.format("[%s].[%s]",
                this.getClass().getSimpleName(),
                this.getClass().getEnclosingMethod().getName());

        if (!success) {
            System.out.printf("   ### [%s] Error%n", methodName);
            throw new RuntimeException(this.getClass().getSimpleName() + " Error");
        }
        System.out.printf("   ### [%s]%n", methodName);

        super.execute(context);
    }
}
