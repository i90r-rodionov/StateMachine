package org.example.fsm.service.fsm.impl;

import lombok.extern.slf4j.Slf4j;
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

import java.util.Locale;

@Service
@SuppressWarnings("all")
@Slf4j
public class DefaultFsmService implements FsmService {

//    private final StateMachinePersister<PurchaseState, PurchaseEvent, String> persister;
    private final StateMachineFactory<FsmState, FsmEvent> stateMachineFactory;
    private final StateHolder stateHolder;
    private Flags flags = Flags.getInstance();


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
        LOG.debug("Start sendEvent");
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
        System.out.printf("   ###### Reset SM to [%s]%n", stateMachine);
        return stateMachine;
    }

    private StateMachineContext<FsmState, FsmEvent> getCxt(FsmState in) {
        StateMachineContext<FsmState, FsmEvent> sd = new DefaultStateMachineContext<>(in, null, null, null);
        return sd;
    }
}
