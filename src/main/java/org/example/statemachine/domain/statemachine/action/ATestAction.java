package org.example.statemachine.domain.statemachine.action;

import org.example.statemachine.domain.statemachine.persist.StateHolder;
import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import static org.example.statemachine.domain.statemachine.state.FsmState.EXIT;


abstract public class ATestAction implements Action<FsmState, FsmEvent> {

    private final StateHolder stateHolder;

    protected ATestAction(StateHolder stateHolder) {
        this.stateHolder = stateHolder;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {
        System.out.println("   ########## " + this.getClass().getSimpleName() + " execute");
        FsmState targetState = context.getTarget().getId();
        if (targetState != EXIT) {
            System.out.println("   ### persist state = " + context.getTarget().getId());
            stateHolder.setState(context.getTarget().getId());
        } else {
            System.out.println("   ### EXIT");
            System.out.println("   ### persist state = " + context.getTarget().getId());
            stateHolder.setState(context.getTarget().getId());

        }
    }
}
