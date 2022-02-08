package org.example.fsm.service.fsm;

import org.example.fsm.service.fsm.dto.FlagsDto;
import org.example.fsm.statemachine.event.FsmEvent;

public interface FsmService {
    boolean test(String id, FsmEvent event);
    boolean restore();
    boolean reset(FlagsDto flags);
    boolean flags(FlagsDto flags);
}
