package org.example.core.statemachine.persist;


import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class StateHolder {
    private FsmState state = FsmState.CREATED;

    private static final Map<UUID, StateMachine<FsmState, FsmEvent>> MAP = new HashMap<>();

    public FsmState getState() {
        return state;
    }

    public void setState(FsmState state) {
        this.state = state;
    }

    public void add(StateMachine<FsmState, FsmEvent> sm) {
        MAP.put(sm.getUuid(), sm);
    }

    public Map<UUID, StateMachine<FsmState, FsmEvent>> getMap() {
        return MAP;
    }
}
