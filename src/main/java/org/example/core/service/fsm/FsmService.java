package org.example.core.service.fsm;

import org.example.core.statemachine.event.FsmEvent;

public interface FsmService {
    boolean sendEvent(FsmEvent event, String id);
    boolean restore();
    boolean reset(String state);
    String flags();
    void flag(String name, String value);
}
