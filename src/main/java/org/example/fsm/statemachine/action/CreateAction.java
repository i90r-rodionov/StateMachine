package org.example.fsm.statemachine.action;

import org.example.fsm.service.mock.MockService;
import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.persist.StateHolder;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;


@Service("createAction")
public class CreateAction extends SaveStateAction {

    private final MockService service;

    public CreateAction(StateHolder stateHolder, MockService service) {
        super(stateHolder);
        this.service = service;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {

        boolean success = service.defaultAction();

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
