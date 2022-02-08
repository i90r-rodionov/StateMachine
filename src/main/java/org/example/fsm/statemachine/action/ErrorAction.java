package org.example.fsm.statemachine.action;


import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class ErrorAction implements Action<FsmState, FsmEvent> {
    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {
        System.out.printf("   ### execute [%s] context=[%s]%n", this.getClass().getSimpleName(), context);
    }
}
