package org.example.statemachine.domain.statemachine.action;


import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class ErrorAction implements Action<FsmState, FsmEvent> {
    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {
        System.out.println("Ошибка при переходе в статус " + context.getTarget().getId());
    }
}
