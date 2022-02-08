package org.example.fsm.service.fsm.impl;

import org.example.fsm.service.fsm.FsmService;
import org.example.fsm.statemachine.state.FsmState;
import org.example.fsm.service.fsm.dto.FlagsDto;
import org.example.fsm.statemachine.guard.Flags;
import org.example.fsm.statemachine.persist.StateHolder;
import org.example.fsm.statemachine.event.FsmEvent;
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
        Flags.setCreatedFolderFlag(flags.getCreatedFolderFlag());
        Flags.setMovedFilesFlag(flags.getMovedFilesFlag());
        stateHolder.setState(flags.getState());
        StateMachine<FsmState, FsmEvent> stateMachine = getSM();
        return true;
    }

    @Override
    public boolean flags(FlagsDto flags) {

        Flags.setNoSignFlag(flags.getNoSignFlag());
        Flags.setCreatedFolderFlag(flags.getCreatedFolderFlag());
        Flags.setMovedFilesFlag(flags.getMovedFilesFlag());

        return true;
    }


    private StateMachine<FsmState, FsmEvent> getSM() {
        final StateMachine<FsmState, FsmEvent> stateMachine = stateMachineFactory.getStateMachine();
        final StateMachineContext<FsmState, FsmEvent> context = getCxt(stateHolder.getState());

        stateMachine.stopReactively().block();
        stateMachine.getStateMachineAccessor().doWithAllRegions(access -> access.resetStateMachine(context));
        stateMachine.startReactively().block();
        stateHolder.add(stateMachine);
        System.out.printf("   ###### Reset SM to [%s]%n", stateMachine);
        return stateMachine;
    }

    private StateMachineContext<FsmState, FsmEvent> getCxt(FsmState in) {
        StateMachineContext<FsmState, FsmEvent> sd = new DefaultStateMachineContext<>(in, null, null, null);
        return sd;
    }
}
