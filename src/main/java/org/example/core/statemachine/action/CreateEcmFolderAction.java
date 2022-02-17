package org.example.core.statemachine.action;

import lombok.extern.slf4j.Slf4j;
import org.example.core.service.mock.CheckService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.persist.StateHolder;
import org.example.core.statemachine.state.FsmState;
import org.example.core.statemachine.util.FsmHelper;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


@Component("createEcmFolderAction")
@Slf4j
public class CreateEcmFolderAction extends BaseAction {

    private final CheckService service;

    public CreateEcmFolderAction(StateHolder stateHolder, CheckService service) {
        super(stateHolder);
        this.service = service;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {


        if (isSla(context)) {
            sendEvent(context, FsmEvent.SLA_EVENT);
            log.debug("   ### Action stop execute {}", this.getClass().getSimpleName());
            return;
        }

        boolean success = service.defaultAction();

        if (!success) {
            generateError();
        }
        System.out.println("   ### Process Successful");

        super.execute(context);

        sendEvent(context, FsmEvent.NEXT_EVENT);


//        final StateMachine<FsmState, FsmEvent> stateMachine = context.getStateMachine();
//        final StateMachineContext<FsmState, FsmEvent> ctx = new DefaultStateMachineContext<>(stateMachine.getState().getId(), null, null, null);
//
//        stateMachine.stopReactively().block();
//        //stateMachine.getStateMachineAccessor().doWithAllRegions(access -> access.resetStateMachine(ctx));
//        stateMachine.startReactively().block();
//        //getStateHolder().add(stateMachine);
//        System.out.printf("   ###### Reset SM [%s] to [%s]%n", stateMachine.getUuid(), stateMachine.getState().getId());
    }

    @Override
    protected boolean isSla(StateContext<FsmState, FsmEvent> context) {
        return (boolean) context.getExtendedState().getVariables().get(FsmHelper.SLA_FLAG);
    }
}
