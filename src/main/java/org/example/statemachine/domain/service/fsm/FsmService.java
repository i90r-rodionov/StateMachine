package org.example.statemachine.domain.service.fsm;

import org.example.statemachine.domain.service.fsm.dto.FlagsDto;
import org.example.statemachine.domain.statemachine.event.FsmEvent;

public interface FsmService {
    boolean test(String id, FsmEvent event);
    boolean restore();
    boolean reset(FlagsDto flags);
    boolean flags(FlagsDto flags);
}
