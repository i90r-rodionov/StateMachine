package org.example.core.statemachine.action;

import org.example.core.service.mock.MockService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.persist.StateHolder;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;


@Component("createEcmFolderAction")
public class CreateEcmFolderAction extends BaseAction {

    private final MockService service;

    public CreateEcmFolderAction(StateHolder stateHolder, MockService service) {
        super(stateHolder);
        this.service = service;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {

        boolean success = service.defaultAction();

        //context.getStateMachine().

        if (!success) {
            generateError();
        }
        System.out.println("   ### Process Successful");

        super.execute(context);

        final StateMachine<FsmState, FsmEvent> stateMachine = context.getStateMachine();
        final StateMachineContext<FsmState, FsmEvent> ctx = new DefaultStateMachineContext<>(stateMachine.getState().getId(), null, null, null);

        stateMachine.stopReactively().block();
        //stateMachine.getStateMachineAccessor().doWithAllRegions(access -> access.resetStateMachine(ctx));
        stateMachine.startReactively().block();
        //getStateHolder().add(stateMachine);
        System.out.printf("   ###### Reset SM [%s] to [%s]%n", stateMachine.getUuid(), stateMachine.getState().getId());
    }
}
