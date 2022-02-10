package org.example.fsm.statemachine.action;

import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.persist.StateHolder;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;


abstract public class BaseAction implements Action<FsmState, FsmEvent> {

    private final StateHolder stateHolder;

    protected BaseAction(StateHolder stateHolder) {
        this.stateHolder = stateHolder;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {
        FsmState targetState = context.getTarget().getId();

        trace();
        System.out.println(String.format("   ### execute [%s] targetState [%s]",
                this.getClass().getSimpleName(), targetState.name()));

        if (targetState != FsmState.EXIT) {
            stateHolder.setState(context.getTarget().getId());
        } else {
            stateHolder.setState(context.getTarget().getId());
        }
    }

    protected void trace() {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        System.out.printf("   *** [%s.%s]%n", this.getClass().getSimpleName(), methodName);

    }
}
