package org.example.fsm.statemachine.persist;

import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.HashMap;

public class FsmPersister implements StateMachinePersist<FsmState, FsmEvent, String> {

    private final HashMap<String, StateMachineContext<FsmState, FsmEvent>> contexts = new HashMap<>();

    @Override
    public void write(final StateMachineContext<FsmState, FsmEvent> context, final String contextObj) {
        contexts.put(contextObj, context);
    }

    @Override
    public StateMachineContext<FsmState, FsmEvent> read(final String contextObj) {
        return contexts.get(contextObj);
    }
}
