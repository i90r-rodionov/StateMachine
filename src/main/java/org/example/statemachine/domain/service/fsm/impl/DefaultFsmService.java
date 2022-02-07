package org.example.statemachine.domain.service.fsm.impl;

import org.example.statemachine.domain.service.fsm.FsmService;
import org.example.statemachine.domain.service.fsm.dto.FlagsDto;
import org.example.statemachine.domain.statemachine.guard.Flags;
import org.example.statemachine.domain.statemachine.persist.StateHolder;
import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
public class DefaultFsmService implements FsmService {

//    private final StateMachinePersister<PurchaseState, PurchaseEvent, String> persister;
    private final StateMachineFactory<FsmState, FsmEvent> stateMachineFactory;
    private final StateHolder stateHolder;


    public DefaultFsmService(
//            StateMachinePersister<PurchaseState, PurchaseEvent, String> persister,
            StateMachineFactory<FsmState, FsmEvent> stateMachineFactory,
            StateHolder stateHolder
    ) {
//        this.persister = persister;
        this.stateMachineFactory = stateMachineFactory;
        this.stateHolder = stateHolder;
    }

    @Override
    public boolean test(String id, FsmEvent event) {

        StateMachine<FsmState, FsmEvent> stateMachine = getSM();
        System.out.println(String.format("   ###### START: SendEvent [%s] to SM=[%s]", event.name(), stateMachine));
        try {
            stateMachine.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Boolean stop = stateMachine.getExtendedState().get("EXIT", Boolean.class);
        if (stop != null && stop) {
//            stateMachine.stop();
        }
        System.out.println(String.format("   ###### DONE: SendEvent [%s] to SM=[%s]", event.name(), stateMachine));
        return true;
    }

    @Override
    public boolean restore() {
        StateMachine<FsmState, FsmEvent> stateMachine = getSM();
        return true;
    }

    @Override
    public boolean reset(FlagsDto flags) {

        Flags.setNoSignFlag(flags.getNoSignFlag());
        Flags.setCreateFolderFlag(flags.getCreateFolderFlag());
        stateHolder.setState(FsmState.CREATE);
        StateMachine<FsmState, FsmEvent> stateMachine = getSM();
        return true;
    }

    @Override
    public boolean flags(FlagsDto flags) {

        Flags.setNoSignFlag(flags.getNoSignFlag());
        Flags.setCreateFolderFlag(flags.getCreateFolderFlag());

        return true;
    }


    private StateMachine<FsmState, FsmEvent> getSM() {
        final StateMachine<FsmState, FsmEvent> stateMachine = stateMachineFactory.getStateMachine();
        final StateMachineContext<FsmState, FsmEvent> context = getCxt(stateHolder.getState());

        stateMachine.stopReactively().block();
        stateMachine.getStateMachineAccessor().doWithAllRegions(access -> access.resetStateMachine(context));
        stateMachine.startReactively().block();
        stateHolder.add(stateMachine);
        System.out.println(String.format("   ###### Reset SM to [%s]", stateMachine));
        return stateMachine;
    }

    private StateMachineContext<FsmState, FsmEvent> getCxt(FsmState in) {
        StateMachineContext<FsmState, FsmEvent> sd = new DefaultStateMachineContext<>(in, null, null, null);
        return sd;
    }
}
