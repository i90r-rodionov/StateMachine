package org.example.core.statemachine;

import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class TestSlaGuard implements Guard<FsmState, FsmEvent> {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
        return true;
    }
}
