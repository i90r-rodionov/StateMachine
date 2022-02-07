package org.example.statemachine.domain.statemachine.action;

import org.example.statemachine.domain.statemachine.persist.StateHolder;
import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import static org.example.statemachine.domain.statemachine.state.FsmState.EXIT;


abstract public class BaseAction implements Action<FsmState, FsmEvent> {

    private final StateHolder stateHolder;

    protected BaseAction(StateHolder stateHolder) {
        this.stateHolder = stateHolder;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {
        FsmState targetState = context.getTarget().getId();

        System.out.println(String.format("   ### execute [%s] targetState [%s]",
                this.getClass().getSimpleName(), targetState.name()));

        if (targetState != EXIT) {
            stateHolder.setState(context.getTarget().getId());
        } else {
            stateHolder.setState(context.getTarget().getId());
        }
    }
}
