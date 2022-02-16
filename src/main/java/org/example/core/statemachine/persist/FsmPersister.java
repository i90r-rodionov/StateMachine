package org.example.core.statemachine.persist;

import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.HashMap;

public class FsmPersister implements StateMachinePersist<FsmState, FsmEvent, String> {

    private static final HashMap<String, StateMachineContext<FsmState, FsmEvent>> FSM_CONTEXT = new HashMap<>();

    @Override
    public void write(final StateMachineContext<FsmState, FsmEvent> context, final String contextObj) {
        FSM_CONTEXT.put(contextObj, context);
    }

    @Override
    public StateMachineContext<FsmState, FsmEvent> read(final String contextObj) {
        return FSM_CONTEXT.get(contextObj);
    }
}
