package org.example.core.service.fsm.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.guard.Flags;
import org.example.core.statemachine.state.FsmState;
import org.example.core.service.fsm.FsmService;
import org.example.core.statemachine.persist.StateHolder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
@Slf4j
public class DefaultFsmService implements FsmService {

    private final StateMachineFactory<FsmState, FsmEvent> stateMachineFactory;
    private final StateMachinePersister<FsmState, FsmEvent, String> persister;
    private final StateHolder stateHolder;
    private Flags flags = Flags.getInstance();


    public DefaultFsmService(
            StateMachineFactory<FsmState, FsmEvent> stateMachineFactory,
            StateMachinePersister<FsmState, FsmEvent, String> persister,
            StateHolder stateHolder

    ) {
        this.stateMachineFactory = stateMachineFactory;
        this.persister = persister;
        this.stateHolder = stateHolder;

    }

    @Override
    public boolean sendEvent(FsmEvent event, String id) {
        LOG.debug("Start sendEvent");
        StateMachine<FsmState, FsmEvent> stateMachine = getSM();

        //StateMachine<FsmState, FsmEvent> stateMachine = stateMachineFactory.getStateMachine();

        try {
            //persister.restore(stateMachine, id);

            System.out.printf("   ###### START: SendEvent [%s] to SM=[%s] [%s]%n", event.name(), stateMachine.getUuid(), stateMachine.getState().getId());

            stateMachine.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Boolean stop = stateMachine.getExtendedState().get("EXIT", Boolean.class);
        if (stop != null && stop) {
            //stateMachine.stop;
        }
        System.out.printf("   ###### DONE: SendEvent [%s] to SM=[%s][%s]%n", event.name(), stateMachine.getUuid(), stateMachine.getState().getId());
        return true;
    }

    @Override
    public boolean restore() {
        StateMachine<FsmState, FsmEvent> stateMachine = getSM();
        return true;
    }

    @Override
    public boolean reset(String state) {
        flags.reset();
        stateHolder.setState(FsmState.valueOf(state.toUpperCase()));
        StateMachine<FsmState, FsmEvent> stateMachine = getSM();
        return true;
    }

    @Override
    public String flags() {
        return flags.getFields();
    }

    @Override
    public void flag(String name, String value) {
        flags.setField(name, Boolean.parseBoolean(value));
    }


    private StateMachine<FsmState, FsmEvent> getSM() {
        final StateMachine<FsmState, FsmEvent> stateMachine = stateMachineFactory.getStateMachine();
        final StateMachineContext<FsmState, FsmEvent> context = getCxt(stateHolder.getState());

        stateMachine.stopReactively().block();
        stateMachine.getStateMachineAccessor().doWithAllRegions(access -> access.resetStateMachine(context));
        stateMachine.startReactively().block();
        stateHolder.add(stateMachine);
        System.out.printf("   ###### Reset SM [%s] to [%s]%n", stateMachine.getUuid(), stateMachine.getState().getId());
        return stateMachine;
    }

    private StateMachineContext<FsmState, FsmEvent> getCxt(FsmState inState) {
        StateMachineContext<FsmState, FsmEvent> context = new DefaultStateMachineContext<>(inState, null, null, null);
        return context;
    }

}
